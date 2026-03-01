# 🎬 Netflix Clone - Spring Boot Full-Stack Application

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-red?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Security-Spring_Security-blue?style=for-the-badge&logo=springsecurity" />
  <img src="https://img.shields.io/badge/Database-MS_SQL_Server-orange?style=for-the-badge&logo=microsoftsqlserver" />
</p>

---

### 🌟 Overview
A high-performance, full-stack Netflix replica built with **Spring Boot**, featuring a robust security layer, automated movie importing from external APIs, and a highly personalized user experience.

---

### 🚀 Key Features

* **🔐 Secure Authentication System**
    * Complete flow: Registration -> Email Activation -> Login.
    * Secure password recovery via email-based verification codes.
* **🛡️ Role-Based Access Control**
    * Custom Security Filter Chain managing permissions.
    * Dedicated **Admin Dashboard** for user management.
* **📥 Automated Content Population**
    * Integrates with **TvMaze API** to bulk-import over 1,000 titles.
    * Stores metadata: original posters, ratings, and cleaned HTML descriptions.
* **🔍 Intelligent Media Discovery**
    * Case-insensitive search and dynamic genre filtering.
    * **Recommendation Engine**: "Similar Movies" using native SQL randomization (`NEWID()`).
* **📺 Smart Media Integration**
    * Automated **YouTube trailer lookup** for movies with missing links.
    * Integrated video player for a seamless viewing experience.
* **💖 Personalized "My List"**
    * Persistent favorite collection stored per user.

---

### 🛠️ Technical Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.x |
| **Security** | Spring Security, BCrypt, JWT Logic |
| **Data** | Spring Data JPA, Hibernate |
| **Database** | MS SQL Server |
| **Mailing** | Spring Mail Sender (SMTP) |
| **Frontend** | Thymeleaf, Bootstrap, JavaScript |
| **API Client** | RestTemplate (TvMaze & YouTube Integration) |

---

### 📂 Architecture
The project follows a **Clean Architecture** pattern, ensuring each component is isolated and easy to maintain:

```text
netflix-clone/
├── ⚙️ config/       # Security & Global Bean Definitions
├── 🎮 controller/   # Web Endpoints (Auth, Admin, Content)
├── 📦 dto/          # Data Transfer Objects & Validation
├── 🏛️ entity/       # JPA Entities (User, Movie)
├── 📑 repository/   # Database Access Layer
└── 🛠️ service/      # Business Logic & API Integrations
⚙️ Quick Start
1. Clone the Repo: git clone https://github.com/Alex-Marin21/Netflix_Clone.git

2. DB Setup: Update application.properties with your SQL Server credentials.

3. SMTP Setup: Configure your email provider for the verification system.

4. Run: Execute mvn spring-boot:run and enjoy!
