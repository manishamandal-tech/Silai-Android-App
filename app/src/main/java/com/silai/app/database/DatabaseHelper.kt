package com.silai.app.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.silai.app.models.Order
import com.silai.app.models.Tailor
import com.silai.app.models.User

/**
 * DatabaseHelper.kt
 * ==================
 * This is the SQLite Database Manager class.
 * It extends SQLiteOpenHelper which provides:
 *   - onCreate()  → called when DB is created for the first time
 *   - onUpgrade() → called when DB version changes
 *
 * VIVA TIP: SQLiteOpenHelper is the standard Android class to manage a local database.
 * Tables: users, tailors, orders
 * All CRUD operations (Create, Read, Update, Delete) are defined here.
 */
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TAG = "DatabaseHelper"

        // Database Info
        const val DATABASE_NAME = "silai_db"
        const val DATABASE_VERSION = 1

        // ---- TABLE: users ----
        const val TABLE_USERS = "users"
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "username"
        const val COL_USER_PASSWORD = "password"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PHONE = "phone"

        // ---- TABLE: tailors ----
        const val TABLE_TAILORS = "tailors"
        const val COL_TAILOR_ID = "id"
        const val COL_TAILOR_NAME = "name"
        const val COL_TAILOR_AREA = "area"
        const val COL_TAILOR_RATING = "rating"
        const val COL_TAILOR_IMAGE = "image_url"
        const val COL_TAILOR_ADDRESS = "address"
        const val COL_TAILOR_DESC = "description"
        const val COL_TAILOR_PHONE = "phone"

        // ---- TABLE: orders ----
        const val TABLE_ORDERS = "orders"
        const val COL_ORDER_ID = "id"
        const val COL_ORDER_USER_ID = "user_id"
        const val COL_ORDER_TAILOR_ID = "tailor_id"
        const val COL_ORDER_TAILOR_NAME = "tailor_name"
        const val COL_ORDER_CLOTH_TYPE = "cloth_type"
        const val COL_ORDER_DATE = "date"
        const val COL_ORDER_TIME = "time"
        const val COL_ORDER_STATUS = "status"
        const val COL_ORDER_INSTRUCTIONS = "instructions"
        const val COL_ORDER_IMAGE_PATH = "image_path"

        // Order Status Constants
        const val STATUS_PICKUP = "Pickup Scheduled"
        const val STATUS_STITCHING = "Stitching in Progress"
        const val STATUS_DELIVERY = "Out for Delivery"
        const val STATUS_DELIVERED = "Delivered"
    }

    // Called ONCE when the database is first created
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "onCreate: Creating database tables")

        // Create Users Table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_NAME TEXT NOT NULL UNIQUE,
                $COL_USER_PASSWORD TEXT NOT NULL,
                $COL_USER_EMAIL TEXT,
                $COL_USER_PHONE TEXT
            )
        """.trimIndent()

        // Create Tailors Table
        val createTailorsTable = """
            CREATE TABLE $TABLE_TAILORS (
                $COL_TAILOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TAILOR_NAME TEXT NOT NULL,
                $COL_TAILOR_AREA TEXT NOT NULL,
                $COL_TAILOR_RATING REAL DEFAULT 4.0,
                $COL_TAILOR_IMAGE TEXT,
                $COL_TAILOR_ADDRESS TEXT,
                $COL_TAILOR_DESC TEXT,
                $COL_TAILOR_PHONE TEXT
            )
        """.trimIndent()

        // Create Orders Table
        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                $COL_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ORDER_USER_ID INTEGER,
                $COL_ORDER_TAILOR_ID INTEGER,
                $COL_ORDER_TAILOR_NAME TEXT,
                $COL_ORDER_CLOTH_TYPE TEXT,
                $COL_ORDER_DATE TEXT,
                $COL_ORDER_TIME TEXT,
                $COL_ORDER_STATUS TEXT DEFAULT '$STATUS_PICKUP',
                $COL_ORDER_INSTRUCTIONS TEXT,
                $COL_ORDER_IMAGE_PATH TEXT,
                FOREIGN KEY($COL_ORDER_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID),
                FOREIGN KEY($COL_ORDER_TAILOR_ID) REFERENCES $TABLE_TAILORS($COL_TAILOR_ID)
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createTailorsTable)
        db.execSQL(createOrdersTable)

        // Insert demo data after creating tables
        insertDemoTailors(db)

        Log.d(TAG, "onCreate: All tables created successfully")
    }

    // Called when DATABASE_VERSION increases
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: Upgrading DB from $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TAILORS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // =============================================
    //  DEMO DATA — 10 Pre-filled Tailors
    //  Images loaded via Glide from Picsum (free)
    // =============================================
    private fun insertDemoTailors(db: SQLiteDatabase) {
        val tailors = listOf(
            arrayOf("Reshma Tailor Works", "Dombivli",
                "4.8",
                "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400",
                "Shop No. 12, Station Road, Dombivli East",
                "Expert in bridal lehengas, blouses & suits. 15+ years experience. Known for perfect fits and intricate embroidery work.",
                "9876543210"),
            arrayOf("Lakshmi Stitching Centre", "Dombivli",
                "4.5",
                "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400",
                "Near Ganesh Temple, Manpada Road, Dombivli West",
                "Specializes in salwar kameez, kurtis and school uniforms. Quick turnaround in 2-3 days.",
                "9123456789"),
            arrayOf("Thane Master Tailor", "Thane",
                "4.7",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                "Opp. Thane Station, Gokhale Road, Thane",
                "Premium men's tailoring — suits, sherwanis, kurta-pyjamas. Trusted by professionals since 2005.",
                "9988776655"),
            arrayOf("Meera Fashion Studio", "Thane",
                "4.6",
                "https://images.unsplash.com/photo-1551803091-e20673f15770?w=400",
                "Vrindavan Society, Majiwada, Thane West",
                "Designer blouses, saree falls, and party wear gowns. Instagram-worthy designs at affordable prices.",
                "9090909090"),
            arrayOf("Kalyan Cloth House", "Kalyan",
                "4.3",
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=400",
                "Main Market Road, Near Bus Stand, Kalyan",
                "All types of stitching including western wear, indo-western fusion, and traditional outfits.",
                "9112233445"),
            arrayOf("Sunita Boutique", "Kalyan",
                "4.9",
                "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400",
                "Adharwadi Road, Kalyan East",
                "Award-winning boutique for bridal wear. Known for finest mirror work and hand embroidery. Minimum 7 days notice for bridal orders.",
                "9222333444"),
            arrayOf("New Bombay Tailors", "Navi Mumbai",
                "4.4",
                "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400",
                "Sector 19, Vashi, Navi Mumbai",
                "Corporate uniforms, formal shirts, trousers. Bulk orders accepted. Free home delivery within Vashi.",
                "9333444555"),
            arrayOf("Navi Fashion Point", "Navi Mumbai",
                "4.2",
                "https://images.unsplash.com/photo-1445205170230-053b83016050?w=400",
                "Near Seawoods Station, Nerul, Navi Mumbai",
                "Affordable daily-wear stitching. Kurtis, churidar, blouses. Ready in 48 hours. Pickup & delivery available.",
                "9444555666"),
            arrayOf("Mumbai Style Hub", "Mumbai",
                "4.8",
                "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=400",
                "Hill Road, Bandra West, Mumbai",
                "Trendy western wear, indo-western designs. Featured in Femina magazine. Bollywood celebrity clientele.",
                "9555666777"),
            arrayOf("Andheri Expert Stitchers", "Mumbai",
                "4.5",
                "https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=400",
                "Lokhandwala Complex, Andheri West, Mumbai",
                "Specializes in office wear & casual clothes. Expert alterations & repairs. Walk-ins welcome 9am–8pm.",
                "9666777888"),
            arrayOf("Pune Silk Tailor", "Pune",
                "4.7",
                "https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?w=400",
                "FC Road, Shivajinagar, Pune",
                "Silk saree blouses, Paithani blouse experts. Traditional Maharashtrian outfits for nauvari sarees.",
                "9777888999"),
            arrayOf("Nashik Couture", "Nashik",
                "4.1",
                "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400",
                "CBS Road, College Road, Nashik",
                "Budget-friendly stitching for students and homemakers. All types accepted. Exchange offers on old orders.",
                "9888999000")
        )

        tailors.forEach { t ->
            val cv = ContentValues().apply {
                put(COL_TAILOR_NAME, t[0])
                put(COL_TAILOR_AREA, t[1])
                put(COL_TAILOR_RATING, t[2].toDouble())
                put(COL_TAILOR_IMAGE, t[3])
                put(COL_TAILOR_ADDRESS, t[4])
                put(COL_TAILOR_DESC, t[5])
                put(COL_TAILOR_PHONE, t[6])
            }
            db.insert(TABLE_TAILORS, null, cv)
        }
        Log.d(TAG, "insertDemoTailors: ${tailors.size} tailors inserted")
    }

    // =============================================
    //  USER CRUD OPERATIONS
    // =============================================

    // INSERT a new user → returns row ID or -1 on failure
    fun registerUser(username: String, password: String, email: String, phone: String): Long {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_USER_NAME, username)
            put(COL_USER_PASSWORD, password)
            put(COL_USER_EMAIL, email)
            put(COL_USER_PHONE, phone)
        }
        return db.insert(TABLE_USERS, null, cv)
    }

    // SELECT user by username+password → returns User or null
    fun loginUser(username: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COL_USER_NAME=? AND $COL_USER_PASSWORD=?",
            arrayOf(username, password),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)) ?: "",
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE)) ?: ""
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    // CHECK if username already exists
    fun isUsernameTaken(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS, arrayOf(COL_USER_ID),
            "$COL_USER_NAME=?", arrayOf(username),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // =============================================
    //  TAILOR CRUD OPERATIONS
    // =============================================

    // SELECT tailors by area
    fun getTailorsByArea(area: String): List<Tailor> {
        val list = mutableListOf<Tailor>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TAILORS, null,
            "$COL_TAILOR_AREA=?", arrayOf(area),
            null, null, "$COL_TAILOR_RATING DESC"
        )
        while (cursor.moveToNext()) {
            list.add(cursorToTailor(cursor))
        }
        cursor.close()
        return list
    }

    // SELECT all tailors
    fun getAllTailors(): List<Tailor> {
        val list = mutableListOf<Tailor>()
        val db = readableDatabase
        val cursor = db.query(TABLE_TAILORS, null, null, null, null, null, COL_TAILOR_RATING + " DESC")
        while (cursor.moveToNext()) {
            list.add(cursorToTailor(cursor))
        }
        cursor.close()
        return list
    }

    // SELECT tailor by ID
    fun getTailorById(id: Int): Tailor? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TAILORS, null,
            "$COL_TAILOR_ID=?", arrayOf(id.toString()),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val t = cursorToTailor(cursor)
            cursor.close()
            t
        } else {
            cursor.close()
            null
        }
    }

    // INSERT a tailor (admin feature)
    fun addTailor(name: String, area: String, rating: Double, imageUrl: String,
                  address: String, desc: String, phone: String): Long {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_TAILOR_NAME, name)
            put(COL_TAILOR_AREA, area)
            put(COL_TAILOR_RATING, rating)
            put(COL_TAILOR_IMAGE, imageUrl)
            put(COL_TAILOR_ADDRESS, address)
            put(COL_TAILOR_DESC, desc)
            put(COL_TAILOR_PHONE, phone)
        }
        return db.insert(TABLE_TAILORS, null, cv)
    }

    // DELETE a tailor
    fun deleteTailor(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_TAILORS, "$COL_TAILOR_ID=?", arrayOf(id.toString()))
    }

    // Helper to convert cursor row → Tailor object
    private fun cursorToTailor(cursor: android.database.Cursor): Tailor {
        return Tailor(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TAILOR_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_TAILOR_NAME)),
            area = cursor.getString(cursor.getColumnIndexOrThrow(COL_TAILOR_AREA)),
            rating = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TAILOR_RATING)),
            imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_TAILOR_IMAGE)) ?: "",
            address = cursor.getString(cursor.getColumnIndexOrThrow(COL_TAILOR_ADDRESS)) ?: "",
            description = cursor.getString(cursor.getColumnIndexOrThrow(COL_TAILOR_DESC)) ?: "",
            phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_TAILOR_PHONE)) ?: ""
        )
    }

    // =============================================
    //  ORDER CRUD OPERATIONS
    // =============================================

    // INSERT a new order
    fun placeOrder(userId: Int, tailorId: Int, tailorName: String,
                   clothType: String, date: String, time: String,
                   instructions: String, imagePath: String): Long {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_ORDER_USER_ID, userId)
            put(COL_ORDER_TAILOR_ID, tailorId)
            put(COL_ORDER_TAILOR_NAME, tailorName)
            put(COL_ORDER_CLOTH_TYPE, clothType)
            put(COL_ORDER_DATE, date)
            put(COL_ORDER_TIME, time)
            put(COL_ORDER_STATUS, STATUS_PICKUP)
            put(COL_ORDER_INSTRUCTIONS, instructions)
            put(COL_ORDER_IMAGE_PATH, imagePath)
        }
        return db.insert(TABLE_ORDERS, null, cv)
    }

    // SELECT orders for a specific user
    fun getOrdersByUser(userId: Int): List<Order> {
        val list = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ORDERS, null,
            "$COL_ORDER_USER_ID=?", arrayOf(userId.toString()),
            null, null, "$COL_ORDER_ID DESC"
        )
        while (cursor.moveToNext()) {
            list.add(cursorToOrder(cursor))
        }
        cursor.close()
        return list
    }

    // SELECT ALL orders (admin)
    fun getAllOrders(): List<Order> {
        val list = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ORDERS, null, null, null, null, null, "$COL_ORDER_ID DESC")
        while (cursor.moveToNext()) {
            list.add(cursorToOrder(cursor))
        }
        cursor.close()
        return list
    }

    // UPDATE order status (admin feature)
    fun updateOrderStatus(orderId: Int, newStatus: String): Int {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_ORDER_STATUS, newStatus)
        }
        return db.update(TABLE_ORDERS, cv, "$COL_ORDER_ID=?", arrayOf(orderId.toString()))
    }

    // DELETE an order
    fun deleteOrder(orderId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_ORDERS, "$COL_ORDER_ID=?", arrayOf(orderId.toString()))
    }

    // Helper to convert cursor row → Order object
    private fun cursorToOrder(cursor: android.database.Cursor): Order {
        return Order(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_ID)),
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_USER_ID)),
            tailorId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_TAILOR_ID)),
            tailorName = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_TAILOR_NAME)) ?: "",
            clothType = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_CLOTH_TYPE)) ?: "",
            date = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_DATE)) ?: "",
            time = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_TIME)) ?: "",
            status = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_STATUS)) ?: STATUS_PICKUP,
            instructions = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_INSTRUCTIONS)) ?: "",
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_IMAGE_PATH)) ?: ""
        )
    }
}