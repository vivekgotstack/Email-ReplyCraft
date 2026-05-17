<div align="center">

# ✨ ReplyCraft

### AI-powered email replies that actually sound human.

Generate polished responses in seconds with customizable tones, secure authentication, and reply history tracking.

<br />

[Live Demo](https://email-reply-craft.vercel.app) • [Tech Stack](#-tech-stack) • [Features](#-features) • [Local Setup](#-run-locally)

</div>

---

# 🚀 Overview

ReplyCraft is a full-stack AI email reply generator built to remove the friction from writing responses.

Paste an email, choose a tone, and instantly generate a clean, context-aware reply.

No bloated workflows.  
No unnecessary complexity.  
Just fast and usable AI assistance.

---

# ✨ Features

<div align="center">

| Feature | Description |
|---|---|
| 🎭 Tone Control | Professional, Friendly, Concise, Persuasive, Sarcastic |
| 🧠 AI Reply Generation | Context-aware responses powered by Gemini |
| 🔐 JWT Authentication | Secure login & registration flow |
| 📜 Reply History | Stores previously generated replies |
| ⚡ Fast Frontend | Built with Vite + React |
| 🎨 Modern UI | Clean dark SaaS-inspired interface |
| 🌐 Full Deployment | Frontend + Backend fully deployed |
| 🛡️ Protected APIs | JWT-secured backend endpoints |
| 📱 Responsive Design | Works across desktop and mobile |

</div>

---

# 🧠 How It Works

```text
User Email Input
       ↓
Tone Selection
       ↓
Spring Boot API
       ↓
Gemini AI Processing
       ↓
Generated Smart Reply
       ↓
Saved To History
```

---

# 🎭 Tone System

ReplyCraft supports multiple response styles:

| Tone | Behavior |
|---|---|
| Professional | Formal, workplace-safe, structured |
| Friendly | Warm and conversational |
| Concise | Short, direct, efficient |
| Persuasive | Convincing and impactful |
| Sarcastic | Sharp and witty |

If no tone is selected, the system defaults to **Professional**.

---

# 🛠 Tech Stack

<div align="center">

## Frontend

React • TypeScript • Vite • TailwindCSS • shadcn/ui • Axios

## Backend

Spring Boot • Spring Security • JWT • PostgreSQL • JPA/Hibernate

## AI

Gemini API (via OpenRouter)

## Deployment

Vercel • Render

</div>

---

# 📸 Application Flow

## 🔑 Authentication

- User registration
- Secure login
- JWT-based authorization
- Stateless backend security

## ✉️ Reply Generation

- Input original email
- Choose tone
- Generate AI reply
- Copy instantly

## 📜 History Tracking

- Stores generated responses
- Pagination support
- View previous replies anytime

---

# 🌐 Live Demo

<div align="center">

## 👉 https://email-reply-craft.vercel.app

</div>

---

# ⚙️ Run Locally

## 📦 Prerequisites

- Java 17+
- Node.js 18+
- Maven
- PostgreSQL

---

# 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/replycraft.git

cd Email-ReplyCraft
```

---

# 2️⃣ Backend Setup

```bash
cd email-reply-writer-backend
```

## Configure Environment Variables

```env
DB_URL=jdbc:postgresql://localhost:5432/email_reply_craft
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

JWT_SECRET=your_super_secret_jwt_key_at_least_32_chars_long

GEMINI_URL=https://openrouter.ai/api/v1/chat/completions
GEMINI_KEY=your_openrouter_api_key

PORT=8080
```

## Run Backend

```bash
./mvnw spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

# 3️⃣ Frontend Setup

```bash
cd email-reply-writer-frontend
```

## Install Dependencies

```bash
npm install
```

## Run Frontend

```bash
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

# 🔐 Security

ReplyCraft uses:

- JWT Authentication
- Stateless Spring Security
- Protected API routes
- BCrypt password hashing
- CORS configuration
- Authentication filters
- Custom exception handling

---

# 📂 Project Structure

```text
Email-ReplyCraft
│
├── email-reply-writer-frontend
│   ├── src
│   ├── components
│   ├── lib
│   └── pages
│
├── email-reply-writer-backend
│   ├── controller
│   ├── service
│   ├── repository
│   ├── security
│   ├── filter
│   ├── entity
│   └── config
```

---

# ⚠️ Important Notes

- Gemini free tier may respond slowly sometimes
- Render backend can cold start after inactivity
- JWT tokens are stored in localStorage
- All secured APIs require authentication
- Generated replies may include placeholders intentionally

Example:

```text
{{PROJECT_NAME}}
{{REPLY_SENDER_NAME}}
```

Replace them before sending.

---

# 🧩 Future Improvements

- Multi-language support
- Streaming AI responses
- Email thread understanding
- Tone blending
- Gmail integration
- Export & templates
- AI personalization memory

---

# 👨‍💻 Author

<div align="center">

## Vivek Nigam

Final-year Computer Science student focused on building production-style full-stack applications with Spring Boot and React.

</div>

---

# 💥 Hosting Mechanism:

<div align="center">

# NeonDB for PostgreSQL, Vercel for Frontend & Render for Backend with Docker environment

</div>

---

<div align="center">

## ⭐ If you like the project, consider giving it a star.

</div>
