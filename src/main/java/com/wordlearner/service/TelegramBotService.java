package com.wordlearner.service;

import com.wordlearner.entity.Word;
import com.wordlearner.entity.AppSettings;
import com.wordlearner.repository.AppSettingsRepository;
import com.wordlearner.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TelegramBotService extends org.telegram.telegrambots.bots.TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    @PostConstruct
    public void init() throws TelegramApiException {
        if (botToken == null || botToken.isEmpty()) {
            System.out.println("Telegram bot token not configured. Set TELEGRAM_BOT_TOKEN environment variable.");
            return;
        }
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(this);
        System.out.println("Telegram bot registered successfully");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        var message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText().trim();

        saveChatId(chatId);

        String response = processCommand(text);
        sendMessage(chatId, response);
    }

    private void saveChatId(Long chatId) {
        AppSettings settings = appSettingsRepository.findById(1L).orElse(new AppSettings());
        settings.setId(1L);
        settings.setTelegramChatId(chatId);
        if (settings.getNotificationTime() == null) {
            settings.setNotificationTime(LocalTime.of(8, 0));
        }
        appSettingsRepository.save(settings);
    }

    private String processCommand(String text) {
        if (text.equalsIgnoreCase("/start")) {
            return getWelcomeMessage();
        }
        if (text.equalsIgnoreCase("/help")) {
            return getHelpMessage();
        }
        if (text.equalsIgnoreCase("/mylist")) {
            return getWordList();
        }
        if (text.toLowerCase().startsWith("/word ")) {
            String word = text.substring(6).trim();
            return lookupWord(word);
        }
        if (text.toLowerCase().startsWith("/delete ")) {
            String word = text.substring(8).trim();
            return deleteWord(word);
        }
        if (text.equalsIgnoreCase("/notify on")) {
            return toggleNotifications(true);
        }
        if (text.equalsIgnoreCase("/notify off")) {
            return toggleNotifications(false);
        }
        if (text.toLowerCase().startsWith("/notifytime ")) {
            String timeStr = text.substring(11).trim();
            return setNotificationTime(timeStr);
        }

        if (!text.startsWith("/")) {
            return lookupWord(text);
        }

        return "Unknown command. Use /help for available commands.";
    }

    private String getWelcomeMessage() {
        return "Welcome to Word Learner Bot!\n\n" +
                "I'll help you learn English words with meanings in both English and Hindi.\n\n" +
                "Commands:\n" +
                "/word <word> - Look up a word\n" +
                "/mylist - View all saved words\n" +
                "/delete <word> - Delete a word\n" +
                "/notify on/off - Toggle notifications\n" +
                "/notifytime HH:MM - Set notification time";
    }

    private String getHelpMessage() {
        return "Commands:\n" +
                "/word <word> - Look up a word\n" +
                "/mylist - View all saved words\n" +
                "/delete <word> - Delete a word\n" +
                "/notify on/off - Toggle notifications\n" +
                "/notifytime HH:MM - Set notification time";
    }

    private String lookupWord(String word) {
        if (word == null || word.isEmpty()) {
            return "Please provide a word. Usage: /word <word>";
        }

        String englishMeaning = dictionaryService.getEnglishMeaning(word);
        String hindiMeaning = dictionaryService.getHindiMeaning(word);

        var existingWord = wordRepository.findByWordIgnoreCase(word);
        String exampleSentence = existingWord.map(Word::getExampleSentence).orElse("");

        if (existingWord.isEmpty()) {
            Word savedWord = new Word(word.toLowerCase(), englishMeaning, hindiMeaning, exampleSentence);
            wordRepository.save(savedWord);
        }

        return buildWordResponse(word, englishMeaning, hindiMeaning, exampleSentence);
    }

    private String buildWordResponse(String word, String english, String hindi, String example) {
        StringBuilder sb = new StringBuilder();
        sb.append("Word: ").append(word.toLowerCase()).append("\n\n");
        sb.append("English Meaning:\n").append(english).append("\n\n");
        sb.append("Hindi Meaning:\n").append(hindi);

        if (example != null && !example.isEmpty()) {
            sb.append("\n\nExample:\n").append(example);
        }

        return sb.toString();
    }

    private String getWordList() {
        List<Word> words = wordRepository.findAll();
        if (words.isEmpty()) {
            return "No words saved yet. Type a word to get its meaning!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Your Word List (").append(words.size()).append(" words):\n\n");
        for (Word word : words) {
            sb.append("- ").append(word.getWord()).append("\n");
        }
        return sb.toString();
    }

    private String deleteWord(String word) {
        var existingWord = wordRepository.findByWordIgnoreCase(word);
        if (existingWord.isPresent()) {
            wordRepository.delete(existingWord.get());
            return "Deleted: " + word.toLowerCase();
        }
        return "Word not found: " + word;
    }

    private String toggleNotifications(boolean enabled) {
        AppSettings settings = appSettingsRepository.findById(1L).orElse(new AppSettings());
        settings.setId(1L);
        settings.setEnabled(enabled);
        appSettingsRepository.save(settings);
        return enabled ? "Notifications enabled" : "Notifications disabled";
    }

    private String setNotificationTime(String timeStr) {
        try {
            LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            AppSettings settings = appSettingsRepository.findById(1L).orElse(new AppSettings());
            settings.setId(1L);
            settings.setNotificationTime(time);
            appSettingsRepository.save(settings);
            return "Notification time set to " + timeStr;
        } catch (Exception e) {
            return "Invalid format. Use HH:MM (e.g., 08:00)";
        }
    }

    public void sendMessage(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    public void sendDailyNotification() {
        AppSettings settings = appSettingsRepository.findById(1L).orElse(null);
        if (settings == null || !settings.getEnabled() || settings.getTelegramChatId() == null) {
            return;
        }

        List<Word> words = wordRepository.findAll();
        if (words.isEmpty()) {
            return;
        }

        Word randomWord = words.get(new Random().nextInt(words.size()));
        String message = "Daily Word Review:\n\n" + buildWordResponse(
                randomWord.getWord(),
                randomWord.getEnglishMeaning(),
                randomWord.getHindiMeaning(),
                randomWord.getExampleSentence()
        );

        sendMessage(settings.getTelegramChatId(), message);

        settings.setLastNotifiedAt(java.time.LocalDateTime.now());
        appSettingsRepository.save(settings);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return "WordLearnerBot";
    }

    public Long getBotId() {
        return null;
    }
}