# Jawaban Ringkas Tugas Asinkronus M11

## Bagian 1: Analisis Awal & Membaca Diagram

### 1. Analisis Kode

* **a. Jika `connect` diubah:** `PasswordReminder` akan **gagal kompilasi**. Baris `dbConnection.connect("jdbc:mysql://...");` di `sendReminders()` akan eror karena jumlah/tipe argumen tidak cocok.
* **b. Jika `executeQuery` mengembalikan `String[]`:** `PasswordReminder` akan **gagal kompilasi**. Baris `List<String> usersToRemind = ...` akan eror *type mismatch*. Solusinya adalah mengubah tipe variabel di `PasswordReminder` menjadi `String[] usersToRemind = ...`.

### 2. Analisis Diagram

* **a. Penyebab Dependensi Kaku:** Baris `this.dbConnection = new MySQLConnection();` di dalam *constructor* `PasswordReminder`. Kelas ini secara eksplisit membuat implementasi detail.
* **b. Kesulitan Testing:** Karena `PasswordReminder` membuat `new MySQLConnection()` sendiri, kita tidak bisa menggantinya dengan objek *mock* (simulasi) saat *unit test*. Pengujian akan selalu mencoba terhubung ke database sungguhan, sehingga gagal jika DB mati dan mengubahnya menjadi *integration test*.

---

## Bagian 2: Eksplorasi Tambahan

### Bagian A: Koneksi "Simulasi"

* **3. Kegunaan `SimulatedConnection`:**
    * **Unit Testing:** Sangat penting untuk menguji logika `PasswordReminder` secara terisolasi tanpa butuh database sungguhan.
    * **Development Offline/Paralel:** Memungkinkan developer bekerja (coding/testing UI) bahkan tanpa koneksi ke database atau saat API/database-nya belum siap.
    * **Demo:** Memastikan demo aplikasi berjalan lancar dan cepat tanpa bergantung pada koneksi jaringan.

### Bagian B: Refleksi Kritis Desain

* **1. "Bau Kode" `connection string`:**
    * **Ya, itu masih "bau kode".** Ini melanggar **Single Responsibility Principle (SRP)**. `PasswordReminder` (logika bisnis) seharusnya tidak perlu tahu detail infrastruktur (seperti alamat database). Jika alamat DB berubah, `PasswordReminder` harus ikut diubah.
* **2. Menghilangkan `connectionString`:**
    * Pindahkan tanggung jawab itu ke luar `PasswordReminder`.
    * **Strategi:** `main` (atau *Composition Root*) harus membaca konfigurasi (misal dari file `.properties` atau `.env`) dan memberikannya ke `PasswordReminder`, entah melalui *constructor* atau sebagai parameter method (cth: `sendReminders(String connectionInfo)`).

---

## Bagian 3: Pembuktian & Refleksi

* **2. Keuntungan *Constructor Injection*:**
    * **Fleksibilitas (Loose Coupling):** Kita bisa menukar implementasi (MySQL, PostgreSQL, Sim) kapan saja tanpa mengubah kode `PasswordReminder`.
    * **Testabilitas:** Memudahkan penyuntikan *mock object* (`SimulatedConnection`) untuk *unit testing*.
    * **Inversi Kontrol (IoC):** Membalik kontrol. `PasswordReminder` tidak lagi membuat dependensinya; ia menerimanya dari luar (misalnya `main`).

* **3. Kaitan ke Diagram "Sesudah Refactoring":**
    * Diagram menunjukkan `PasswordReminder` kini hanya bergantung pada `DBConnectionInterface`.
    * Kode `main` membuktikan ini: kita membuat `PasswordReminder` yang sama dan menyuntikkan `MySQLConnection` di Skenario 1 dan `PostgreSQLConnection` di Skenario 2.
    * `PasswordReminder` tetap bekerja tanpa diubah, membuktikan ia tidak peduli pada implementasi konkret, tapi hanya pada "kontrak" (`DBConnectionInterface`).