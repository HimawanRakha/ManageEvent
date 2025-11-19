# ManageEvent Backend

Spring Boot backend untuk aplikasi event management.

## 🚀 Quick Start

### Local Development

```bash
# Install dependencies dan run
mvn spring-boot:run

# Atau build JAR dan run
mvn clean package
java -jar target/ManageEvent-0.0.1-SNAPSHOT.jar
```

Server akan berjalan di `http://localhost:8080`

### Environment Variables

- `MONGODB_URI`: MongoDB connection string (optional, ada default)
- `PORT`: Server port (default: 8080)
- `ALLOWED_ORIGINS`: CORS allowed origins, dipisah koma (optional)

## 📦 Deploy

Lihat [DEPLOYMENT.md](./DEPLOYMENT.md) untuk panduan lengkap deployment ke Railway, Render, atau platform lainnya.

### Quick Deploy ke Railway

1. Push code ke GitHub
2. Buka https://railway.app
3. New Project → Deploy from GitHub
4. Set environment variables:
   - `MONGODB_URI`: MongoDB connection string
   - `ALLOWED_ORIGINS`: URL Vercel Anda (contoh: `https://your-app.vercel.app`)
5. Deploy!

## 📚 API Endpoints

- `GET /api/events` - Get all events
- `GET /api/events/{id}` - Get event by ID
- `POST /api/events` - Create event
- `PUT /api/events/{id}` - Update event
- `DELETE /api/events/{id}` - Delete event
- `POST /api/auth/register` - Register user
- `POST /api/auth/login` - Login user
- `GET /api/registrations/my-events?userId={id}` - Get user events
- `POST /api/registrations` - Register for event
- `DELETE /api/registrations?userId={id}&eventId={id}` - Cancel registration

## 🛠️ Tech Stack

- Spring Boot 3.5.7
- MongoDB
- Spring Security
- Java 17

