# JWT Authentication & File Upload System

A secure Spring Boot 3 application implementing JWT-based authentication with file upload/download capabilities. Users can register, login, logout, and manage their personal files through RESTful APIs.

## 🚀 Features

### Authentication
- ✅ User registration with email validation
- ✅ JWT-based login system
- ✅ Secure logout with token blacklisting
- ✅ Password encryption using BCrypt
- ✅ Protected API endpoints

### File Management
- ✅ Secure file upload to static directory
- ✅ File download with proper headers
- ✅ File listing for authenticated users
- ✅ File deletion with cleanup
- ✅ File type and size validation
- ✅ User-specific file isolation

### Security
- ✅ JWT token validation
- ✅ Token blacklist for logout
- ✅ User-based file access control
- ✅ Input validation and sanitization

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Security**: Spring Security 6 + JWT
- **Database**: H2 (In-Memory)
- **ORM**: Spring Data JPA
- **File Storage**: Local file system (`static/images`)
- **Build Tool**: Maven
- **Java**: 17+

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🏃‍♂️ Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/abdulawr/JWT-and-File-Uploading-in-Spring-Boot.git
cd jwt-auth-file-system
```

### 2. Build the Application
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Create Static Directory
Create the following directory structure:
```
src/main/resources/static/images/
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```
**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "johndoe",
  "email": "john@example.com"
}
```

#### Logout User
```http
POST /api/auth/logout
Authorization: Bearer <your-jwt-token>
```

#### Get User Profile
```http
GET /api/auth/profile
Authorization: Bearer <your-jwt-token>
```

### File Management Endpoints

#### Upload File
```http
POST /api/files/upload
Authorization: Bearer <your-jwt-token>
Content-Type: multipart/form-data

file: <your-file>
```

#### List User Files
```http
GET /api/files/my-files
Authorization: Bearer <your-jwt-token>
```

#### Download File
```http
GET /api/files/download/{fileId}
Authorization: Bearer <your-jwt-token>
```

#### View File (Inline)
```http
GET /api/files/view/{fileId}
Authorization: Bearer <your-jwt-token>
```

#### Delete File
```http
DELETE /api/files/{fileId}
Authorization: Bearer <your-jwt-token>
```

#### Get File Info
```http
GET /api/files/info/{fileId}
Authorization: Bearer <your-jwt-token>
```

## 🗄️ Database Access

### H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Database Schema

#### Users Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| username | VARCHAR(50) | Unique username |
| email | VARCHAR(100) | Unique email |
| password | VARCHAR(100) | Encrypted password |
| role | VARCHAR(20) | User role (USER/ADMIN) |

#### File_Info Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| file_name | VARCHAR(255) | System-generated filename |
| original_file_name | VARCHAR(255) | Original filename |
| content_type | VARCHAR(100) | MIME type |
| file_size | BIGINT | File size in bytes |
| file_path | VARCHAR(500) | Web-accessible path |
| upload_time | TIMESTAMP | Upload timestamp |
| uploaded_by | BIGINT | Foreign key to users |

## ⚙️ Configuration

### Application Properties
```yaml
# Database Configuration
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: password
  
# JWT Configuration
jwt:
  secret: myVeryLongSecretKeyThatIsAtLeast256BitsLongForJWTSecurity123456789
  expiration: 86400000 # 24 hours

# File Upload Configuration
file:
  upload-dir: src/main/resources/static/images
  max-size: 10485760 # 10MB
  allowed-types: jpg,jpeg,png,gif,pdf,txt,doc,docx
```

### Customize Settings
- **JWT Secret**: Change the JWT secret for production
- **File Size Limit**: Modify `file.max-size` property
- **Allowed File Types**: Update `file.allowed-types` property
- **Upload Directory**: Change `file.upload-dir` property

## 🧪 Testing with cURL

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

### 3. Upload File
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@path/to/your/file.jpg"
```

### 4. List Files
```bash
curl -X GET http://localhost:8080/api/files/my-files \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Logout
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── config/          # Security & Web configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── filter/         # JWT authentication filter
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/
│       ├── static/images/  # File upload directory
│       └── application.yml # Configuration
└── test/                   # Test files
```

## 🔒 Security Features

- **Password Encryption**: BCrypt hashing for secure password storage
- **JWT Tokens**: Secure token-based authentication
- **Token Blacklist**: Logout invalidates tokens server-side
- **File Access Control**: Users can only access their own files
- **Input Validation**: Request validation using Bean Validation
- **File Type Restriction**: Only allowed file types can be uploaded
- **File Size Limits**: Configurable maximum file size

## 🐛 Common Issues & Solutions

### Issue: JWT Key Too Short
```
Error: The specified key byte array is 88 bits which is not secure enough
```
**Solution**: Use a JWT secret key that's at least 32 characters long.

### Issue: Upload Directory Not Found
```
Error: Failed to create upload directory
```
**Solution**: Manually create the `src/main/resources/static/images/` directory.

### Issue: File Not Found After Upload
**Solution**: Check the file path configuration and ensure the WebConfig is properly mapping static resources.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


## 👨‍💻 Author

Abdul Basit - [tcomprog@gmail.com](mailto:your.email@example.com)

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- JWT.io for JWT implementation guidance
- Spring Security documentation
- H2 Database for easy development database setup

---

⭐ **Star this repo if you found it helpful!** ⭐