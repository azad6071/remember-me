# Word Learning Telegram Bot - Project Plan

## 1. Project Overview

A Telegram bot that helps you learn and track English vocabulary with meanings in both English and Hindi, featuring daily word review notifications.

## 2. Tech Stack

- **Backend**: Spring Boot 3.x
- **Database**: H2 (file-based, in-memory)
- **Language**: Java 17
- **Build**: Maven
- **Telegram**: Telegram Bot API (java-telegram-bot-api library)

## 3. Core Features

### 3.1 Word Lookup
- User sends a word to the bot
- Bot returns meaning in English and Hindi
- Word is saved to user's word list

### 3.2 Daily Random Word Notification
- Scheduled job runs daily at configured time
- Selects random word from user's saved words
- Sends notification to user

### 3.3 Word List Management
- View all saved words
- Delete specific words

## 4. Architecture

```
┌─────────────┐     ┌─────────────────┐     ┌─────────────┐
│  Telegram   │────▶│  Spring Boot    │────▶│    H2      │
│  User       │◀────│    Bot API       │◀────│  Database  │
└─────────────┘     └─────────────────┘     └─────────────┘
                           │
                    ┌──────┴──────┐
                    │ Dictionary  │
                    │ API Service │
                    └─────────────┘
```

## 5. Database Schema

### Table: words
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto-increment ID |
| word | VARCHAR(255) | The English word |
| english_meaning | TEXT | English definition |
| hindi_meaning | TEXT | Hindi definition |
| example_sentence | TEXT | Example usage (optional) |
| created_at | TIMESTAMP | When word was added |

### Table: app_settings
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Always 1 (singleton) |
| enabled | BOOLEAN | Notifications on/off |
| notification_time | TIME | Daily notification time |
| last_notified_at | TIMESTAMP | Last notification sent |
| telegram_chat_id | BIGINT | Your Telegram chat ID |

## 6. API Integrations

### Dictionary API
- **Free Dictionary API** (https://dictionaryapi.dev) - English meanings, pronunciation, examples
- **MyMemory API** (https://mymemory.translated.net) - Free translation to Hindi (no API key needed)

## 7. Telegram Bot Commands

| Command | Description |
|---------|-------------|
| /start | Get welcome message |
| /word \<word\> | Look up a word |
| /mylist | View all saved words |
| /delete \<word\> | Delete a word from list |
| /notify on/off | Toggle daily notifications |
| /notifytime HH:MM | Set notification time |
| /help | Show help message |

## 8. Project Structure

```
remember-me/
├── pom.xml
├── src/main/java/com/wordlearner/
│   ├── WordLearnerApplication.java
│   ├── entity/
│   │   ├── Word.java
│   │   └── AppSettings.java
│   ├── repository/
│   │   ├── WordRepository.java
│   │   └── AppSettingsRepository.java
│   ├── service/
│   │   ├── TelegramBotService.java
│   │   ├── DictionaryService.java
│   │   └── WordService.java
│   └── scheduler/
│       └── DailyNotificationScheduler.java
├── src/main/resources/
│   ├── application.properties
│   └── data.sql (optional)
└── README.md
```

## 9. Implementation Phases

### Phase 1: Setup (Day 1)
- Create Spring Boot project with dependencies
- Configure H2 database
- Set up logging

### Phase 2: Bot Core (Day 2-3)
- Implement Telegram bot commands
- Basic word lookup with mock data
- Set your Telegram chat ID automatically on first message

### Phase 3: Dictionary Integration (Day 4)
- Integrate Free Dictionary API
- Add Hindi meaning lookup (fallback to manual)

### Phase 4: Notifications (Day 5)
- Implement scheduled notification job
- Add notification settings management

### Phase 5: Polish (Day 6)
- Error handling
- Edge cases
- Testing

## 10. Configuration (application.properties)

```properties
#Server
server.port=8080

#H2 Database
spring.datasource.url=jdbc:h2:file:./data/wordlearner
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

#Telegram Bot
telegram.bot.token=${TELEGRAM_BOT_TOKEN}

#Scheduler
scheduler.daily-notification.enabled=true
scheduler.daily-notification.time=09:00
```

## 11. API Response Formats

### Dictionary API Response (English)
```json
{
  "word": "eloquent",
  "meaning": " fluent or persuasive in speaking or writing",
  "example": "an eloquent speech"
}
```

### Telegram Message Response
```
📚 Word: eloquent

🇺🇸 English Meaning:
Fluent or persuasive in speaking or writing.

🇮🇳 Hindi Meaning:
सुंदर और प्रभावी ढंग से बोलने या लिखने में कुशल।

📝 Example:
She gave an eloquent speech that moved the audience.
```

## 12. Future Enhancements (Optional)

- Quiz mode with multiple choice
- Spaced repetition learning
- Word categories/topics
- Progress tracking
- Web dashboard
- Mobile app

## 13. Known Challenges

1. **Hindi Dictionary API**: Finding free reliable Hindi dictionary API. May need fallback to manual/empty.
2. **Scheduled Notifications**: Need to ensure bot is running 24/7 or use external scheduler.
3. **Telegram Webhooks vs Polling**: Webhooks preferrable for production but requires HTTPS域名. For local dev, polling works fine.

## 14. Getting Started Prerequisites

1. Create Telegram bot via @BotFather
2. Get bot token
3. Configure environment variable or application.properties

---

Let me know if you'd like me to start implementing Phase 1 or adjust any of these details!