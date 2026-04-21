# Word Learner Bot

A Telegram bot that helps you learn English vocabulary with meanings in both English and Hindi, featuring daily word review notifications.

## Features

- Look up any English word to get its meaning in English and Hindi
- Automatically saves words to your personal word list
- Daily random word notification at a configured time
- Manage your word list (view, delete words)
- Toggle notifications on/off
- Set custom notification time

## Tech Stack

- Spring Boot 2.7
- H2 Database (file-based)
- Telegram Bot API

---

## Telegram Bot Setup

### Step 1: Create a Bot

1. Open Telegram and search for **@BotFather**
2. Start the conversation and send `/newbot`
3. Follow the prompts:
   - Enter a name for your bot (e.g., "Word Learner")
   - Enter a username ending in `bot` (e.g., `MyWordLearnerBot`)
4. **Copy the bot token** - it will look like: `1234567890:ABCdefGHIjklMNOpqrsTUVwxyz`

### Step 2: Find Your Chat ID

1. Search for your bot username and start a chat
2. Send `/start` to the bot
3. (Optional) To find your chat ID programmatically, send a message to the bot, then visit:
   ```
   https://api.telegram.org/bot<TOKEN>/getUpdates
   ```
   Or forward a message from your bot to @userinfobot on Telegram

### Step 3: Configure the Environment

Set the bot token before running the app:

```bash
export TELEGRAM_BOT_TOKEN=your_bot_token_here
```

Or on Windows (Command Prompt):
```cmd
set TELEGRAM_BOT_TOKEN=your_bot_token_here
```

Or on Windows (PowerShell):
```powershell
$env:TELEGRAM_BOT_TOKEN="your_bot_token_here"
```

---

## Running the App

### Option 1: Maven

```bash
mvn spring-boot:run
```

### Option 2: Build JAR

```bash
mvn package
java -jar target/remember-me-1.0.0.jar
```

### Access H2 Console (Database Browser)

The app includes an H2 database console at: **http://localhost:8080/h2-console**

- JDBC URL: `jdbc:h2:file:./data/wordlearner`
- Username: `sa`
- Password: *(leave empty)*

---

## Bot Commands

| Command | Description |
|---------|-------------|
| `/start` | Welcome message |
| `/help` | Show available commands |
| `/word <word>` | Look up a word |
| Just type a word | Look up a word (shorthand) |
| `/mylist` | View all saved words |
| `/delete <word>` | Delete a word |
| `/notify on` | Enable daily notifications |
| `/notify off` | Disable notifications |
| `/notifytime HH:MM` | Set notification time (e.g., 08:00) |

### Example Usage

1. Open your bot in Telegram
2. Send `/start` to register
3. Type any word (e.g., `hello`) to get meanings
4. Send `/mylist` to see saved words
5. Send `/notify on` to enable daily reminders
6. Send `/notifytime 09:00` to set notification at 9 AM

---

## Project Structure

```
src/main/java/com/wordlearner/
├── WordLearnerApplication.java    # Main application
├── entity/
│   ├── Word.java                 # Word entity
│   └── AppSettings.java          # App settings entity
├── repository/
│   ├── WordRepository.java
│   └── AppSettingsRepository.java
├── service/
│   ├── TelegramBotService.java    # Bot logic
│   └── DictionaryService.java   # Dictionary API calls
└── scheduler/
    └── DailyNotificationScheduler.java
```

---

## API Integrations

- English meanings: [Free Dictionary API](https://dictionaryapi.dev)
- Hindi translations: Google Translate API

---

## Notes

- The first time you message the bot, your chat ID is automatically saved
- Notifications run daily at 8 AM by default (configurable with `/notifytime`)
- Words are stored in a local H2 database file (`data/wordlearner.mv.db`)
- The database file is ignored by git (see `.gitignore`)