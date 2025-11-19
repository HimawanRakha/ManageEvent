# Panduan Deploy Spring Boot Backend

Panduan lengkap untuk deploy backend Spring Boot ke production.

## 📋 Persiapan

### 1. Update CORS untuk Production

Pastikan `SecurityConfig.java` sudah diupdate untuk mendukung domain Vercel Anda.

### 2. Environment Variables yang Diperlukan

- `MONGODB_URI`: Connection string MongoDB (sudah ada di MongoDB Atlas)
- `PORT`: Port server (biasanya otomatis di-set oleh platform)
- `ALLOWED_ORIGINS`: Domain Vercel Anda (opsional, contoh: `https://your-app.vercel.app`)

---

## 🚀 Opsi 1: Deploy ke Railway (Recommended - Paling Mudah)

Railway adalah platform yang sangat mudah untuk deploy Spring Boot.

### Langkah-langkah:

1. **Daftar di Railway**

   - Kunjungi https://railway.app
   - Sign up dengan GitHub

2. **Create New Project**

   - Klik "New Project"
   - Pilih "Deploy from GitHub repo"
   - Pilih repository yang berisi folder `ManageEvent`

3. **Setup Service**

   - Railway akan auto-detect Java/Maven
   - Pastikan root directory di-set ke `ManageEvent` (bukan root repo)

4. **Set Environment Variables**

   - Klik pada service → Variables tab
   - Tambahkan:
     ```
     MONGODB_URI=mongodb+srv://himawanrakha11_db_user:Kacang123polong@eventpenalaran.hzx3xt7.mongodb.net/EventPenalaran?retryWrites=true&w=majority&appName=EventPenalaran
     ALLOWED_ORIGINS=https://your-app.vercel.app,http://localhost:3000
     ```

5. **Deploy**

   - Railway akan otomatis build dan deploy
   - Tunggu hingga selesai (biasanya 3-5 menit)

6. **Dapatkan URL**
   - Setelah deploy, Railway akan memberikan URL seperti: `https://your-app.up.railway.app`
   - Copy URL ini untuk digunakan di Next.js

---

## 🚀 Opsi 2: Deploy ke Render

Render juga mudah digunakan dan memiliki free tier.

### Langkah-langkah:

1. **Daftar di Render**

   - Kunjungi https://render.com
   - Sign up dengan GitHub

2. **Create New Web Service**

   - Klik "New +" → "Web Service"
   - Connect GitHub repository
   - Pilih repository dan branch

3. **Configure Service**

   - **Name**: `manageevent-backend`
   - **Root Directory**: `ManageEvent`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/ManageEvent-0.0.1-SNAPSHOT.jar`

4. **Set Environment Variables**

   - Scroll ke bagian "Environment Variables"
   - Tambahkan:
     ```
     MONGODB_URI=mongodb+srv://himawanrakha11_db_user:Kacang123polong@eventpenalaran.hzx3xt7.mongodb.net/EventPenalaran?retryWrites=true&w=majority&appName=EventPenalaran
     ALLOWED_ORIGINS=https://your-app.vercel.app,http://localhost:3000
     ```

5. **Deploy**

   - Klik "Create Web Service"
   - Tunggu build dan deploy selesai

6. **Dapatkan URL**
   - Render akan memberikan URL seperti: `https://manageevent-backend.onrender.com`

---

## 🚀 Opsi 3: Deploy dengan Docker

Jika ingin lebih kontrol, bisa pakai Docker.

### Build Docker Image:

```bash
cd ManageEvent
docker build -t manageevent-backend .
```

### Run Locally (Testing):

```bash
docker run -p 8080:8080 \
  -e MONGODB_URI="your-mongodb-uri" \
  -e ALLOWED_ORIGINS="https://your-app.vercel.app" \
  manageevent-backend
```

### Deploy ke Platform yang Support Docker:

- **Fly.io**: https://fly.io
- **DigitalOcean App Platform**: https://www.digitalocean.com/products/app-platform
- **AWS ECS/Fargate**: https://aws.amazon.com/ecs

---

## 🔧 Update Next.js untuk Production

Setelah backend ter-deploy, update Next.js di Vercel:

1. **Buka Vercel Dashboard**

   - Pilih project Anda
   - Klik "Settings" → "Environment Variables"

2. **Tambahkan Environment Variable**

   ```
   NEXT_PUBLIC_BACKEND_URL=https://your-backend-url.railway.app
   ```

   (Ganti dengan URL backend Anda yang sebenarnya)

3. **Redeploy**
   - Klik "Deployments" → "Redeploy" untuk apply environment variable baru

---

## ✅ Testing Production

Setelah deploy, test endpoint:

```bash
# Test health check
curl https://your-backend-url.railway.app/api/events

# Test dengan browser
# Buka: https://your-backend-url.railway.app/api/events
```

---

## 🔍 Troubleshooting

### CORS Error

- Pastikan `ALLOWED_ORIGINS` sudah di-set dengan benar
- Pastikan URL Vercel sudah ditambahkan (tanpa trailing slash)

### MongoDB Connection Error

- Pastikan MongoDB Atlas sudah allow connection dari semua IP (0.0.0.0/0)
- Atau tambahkan IP Railway/Render ke whitelist MongoDB Atlas

### Port Error

- Pastikan menggunakan environment variable `PORT` (Railway/Render otomatis set ini)
- Jangan hardcode port di code

### Build Error

- Pastikan Java 17 tersedia di platform
- Check build logs untuk detail error

---

## 📝 Checklist Sebelum Deploy

- [ ] MongoDB Atlas sudah dikonfigurasi dengan benar
- [ ] Environment variables sudah di-set
- [ ] CORS sudah dikonfigurasi untuk domain Vercel
- [ ] Build berhasil di local (`mvn clean package`)
- [ ] Test API di local berhasil
- [ ] URL backend sudah di-set di Vercel environment variables

---

## 🎉 Selesai!

Setelah semua langkah selesai, aplikasi Anda sudah siap digunakan di production!
