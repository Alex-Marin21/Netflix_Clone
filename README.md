Netflix Clone - Spring Boot Application
A high-performance, full-stack Netflix replica built with Spring Boot, featuring a robust security layer, automated movie importing from external APIs, and a personalized user experience.

🚀 Key Features
Secure Authentication System: Complete flow including user registration, account activation via email codes, and password recovery.

Role-Based Access Control: Strict security configurations using Spring Security, with dedicated dashboards for administrators and protected routes for authenticated users.

Automated Content Population: Integrates with the TvMaze API to bulk-import over 1,000 titles, including metadata like descriptions, ratings, and original posters.

Intelligent Media Discovery:

Search functionality with case-insensitive title matching.

Dynamic genre filtering.

"Similar Movies" recommendation engine using native SQL randomization.

Personalized "My List": Users can manage a persistent collection of favorite movies stored in a relational database.

Smart Trailer Integration: Automated YouTube trailer lookup for movies with missing media links via a specialized service.

🛠️ Technical Stack
Backend: Java 17+, Spring Boot 3.x, Spring Security.

Persistence: Spring Data JPA, Hibernate.

Database: Designed for MS SQL Server (utilizes NEWID() for randomizing results).

Mailing: Spring Boot Starter Mail for verification and reset tokens.

Frontend: Thymeleaf template engine with dynamic attribute binding.

API Client: RestTemplate for external service communication.

📂 Project Structure
The project follows a clean, folder-per-component architecture for maximum maintainability:

Plaintext

src/main/java/com/netflixclone/netflix_clone/
├── config/       # Security and global bean definitions
├── controller/   # Web controllers for Auth, Admin, and Content
├── dto/          # Data Transfer Objects for validation and API mapping
├── entity/       # JPA Entities (User, Movie)
├── repository/   # Data access interfaces
└── service/      # Business logic and external integrations
⚙️ Setup & Installation
Clone the repository:

Bash

git clone https://github.com/Alex-Marin21/Netflix_Clone.git
Configure Database: Update src/main/resources/application.properties with your MS SQL Server credentials.

Mail Server: Configure your SMTP settings in application.properties to enable account verification features.

Run the application:

Bash

mvn spring-boot:run
Import Content: The application is equipped with a MovieImportService. You can trigger the importMovies() method to populate your local database with initial data.

🔐 Security Overview
The application implements a custom UserDetailsService that maps database users to Spring Security principals, appending a ROLE_ prefix to user roles.

Passwords: Encrypted using BCrypt.

Verification: Accounts are disabled by default until the correct 6-digit code is provided.
