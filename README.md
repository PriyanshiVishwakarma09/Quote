# ❝ Quote

<div align="center">

  ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
  ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
  ![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
  ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

  **A Premium, Quote Discovery App for Android**

</div>

---

## ✨ Overview

**Quote** reimagines the quote collection experience. Moving away from standard Material Design, it embraces a **"Dark Glassmorphism"** aesthetic—featuring deep navy gradients, frosted glass UI elements, and glowing accents.

Beyond its looks, it is a fully functional cloud-connected app featuring **real-time sync**, **daily background updates**, and **home screen widgets**.

---

## 🎨 Design Philosophy: "Dark Glass"

The UI was crafted to feel premium and immersive.

* 🌌 **Deep Navy Gradient:** Instead of flat black, we use a vertical gradient (`#1A1A2E` → Black) to add depth on OLED screens.
* 🧊 **Glassmorphism:** Cards and inputs use `Color.White` with **8% opacity**, mimicking frosted glass that lets the background shine through.
* 💡 **Tactile Accents:** The navigation bar features "glowing" pink pill indicators (`#E94057`), creating a cyberpunk-inspired focus state.
* 📖 **Typography:** Elegant **Serif fonts** for quotes evoke a classic book feel, contrasted with modern Sans-Serif headers.

---

## 📱 Key Features

### 🔐 **Authentication & Cloud**
* **Supabase Auth (v3):** Secure Email/Password login.
* **Smart Profiles:** Auto-generates display names from email addresses.
* **Real-time Favorites:** Tapping 'Heart' instantly updates the UI (Optimistic UI) while syncing to PostgreSQL in the background.

### 📅 **Daily Inspiration Engine**
* **24-Hour Cycle:** A custom `DailyQuoteManager` locks the "Quote of the Day" for 24 hours.
* **Smart Notifications:** Uses **WorkManager** to schedule alerts. If the user's preferred time has passed, it intelligently schedules for the next day.
* **Home Screen Widget:** Built with **Jetpack Glance**, sharing data with the main app to show daily wisdom at a glance.

### ⚙️ **Personalization**
* **DataStore Preferences:** Asynchronous storage for settings.
* **Customization:** Dark Mode toggle and precise Notification Time picker.

---

## 🛠️ Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material3) |
| **Backend** | Supabase (Auth + Database) |
| **Asynchronous** | Coroutines & Flow |
| **Background** | WorkManager |
| **Widget** | Jetpack Glance |
| **Storage** | Jetpack DataStore |
| **Architecture** | MVVM (Model-View-ViewModel) |

---

## 🤖 AI-First Workflow

This project was built using an **AI-Assisted Pair Programming** model. Rather than auto-generating generic code, AI was used to solve specific architectural hurdles.

### 1. Migration Solved: Supabase v3

**The Challenge:** Most tutorials use the deprecated `signInWithPassword` (v2).

**The Fix:** I fed compiler errors to the AI, which correctly identified the new Unified Provider syntax:

// New v3.0 Syntax identified via AI debugging
auth.signInWith(Email) {
    this.email = userEmail
    this.password = userPass
}

<h3>2. 🧩 Boilerplate Crushed: Glance Widgets</h3>
<p>
  <strong>The Challenge:</strong> Android Widgets require complex XML + Receiver boilerplate.<br>
  <strong>The Fix:</strong> AI generated the verbose <code>xml/quote_widget_info.xml</code> and Receiver class, allowing me to focus strictly on the Compose UI logic.
</p>

<h3>3. 🎨 Design Refined: Glassmorphism</h3>
<p>
  <strong>The Challenge:</strong> Guessing the correct Alpha values for "frosted glass" is tedious.<br>
  <strong>The Fix:</strong> Prompted specific constraints (<em>"Card with 8% white opacity and 10% border stroke"</em>), yielding the perfect Brush and Color definitions used in <code>QuoteCard.kt</code>.
</p>

<hr>

<h2>🚀 Getting Started</h2>

<ol>
  <li>
    <strong>Clone & Open</strong>
    <pre><code>git clone https://github.com/PriyanshiVishwakarma09/Quote.git</code></pre>
  </li>

  <li>
    <strong>Configure Supabase</strong>
    <p>Run this SQL in your Supabase project to set up the database and security policies:</p>
    <pre><code>create table public.favorites (
  id uuid not null default gen_random_uuid (),
  user_id uuid not null references auth.users (id) on delete cascade,
  quote_id text not null,
  created_at timestamp with time zone not null default now(),
  primary key (id)
);

-- Enable Row Level Security (RLS)
alter table public.favorites enable row level security;

-- Policies
create policy "User View Own" on favorites for select using (auth.uid() = user_id);
create policy "User Add Own" on favorites for insert with check (auth.uid() = user_id);
create policy "User Delete Own" on favorites for delete using (auth.uid() = user_id);</code></pre>
  </li>

  <li>
    <strong>Add Keys</strong>
    <p>Update <code>SupabaseClient.kt</code> with your project details:</p>
    <pre><code>const val SUPABASE_URL = "https://YOUR_PROJECT.supabase.co"
const val SUPABASE_KEY = "YOUR_ANON_KEY"</code></pre>
  </li>
</ol>

<hr>

<h2>⚠️ Known Limitations</h2>
<ul>
  <li><strong>Search:</strong> Visual-only implementation (filtering planned for v2).</li>
  <li><strong>Avatars:</strong> Currently uses initial-based placeholders.</li>
  <li><strong>Offline:</strong> Full offline caching for the feed is in development.</li>
  <li><strong>Offline:</strong> Themes set Up is still not completed.</li>
</ul>

<hr>

<div align="center">
  <strong>Built with ❤️ and 🤖 for the AI Proficiency Assignment</strong>
</div>
