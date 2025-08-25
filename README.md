# FridgeMate

レシートを撮影し、OCR解析で自動登録できるAndroidアプリです。
U-22プログラミングコンテスト2025 出展作品。

---

## 開発環境
- Android Studio Narwhal Feature Drop | 2025.1.2
- Kotlin 1.9.20
- Android Gradle Plugin 8.6.0
- minSdk 26 / targetSdk 35
- 
## 動作環境
- 動作確認端末: Pixel 6a (Android 16)

## ビルド方法
1. Android Studio で本プロジェクトを開く
2. local.properties に SERVER_URL=yur-server-url/ を追記
   サーバーURLはバックエンド起動時に表示されます。例）http://192.168.50.77:5000/
3. local.properties に以下を追記
   BASE_URL = https://app.rakuten.co.jp/services/api/Recipe/CategoryRanking/20170426
   APP_ID = 1081684173276999312
4. Gradle Sync を実行 
5. Run でビルド・インストール可能

## 注意事項
- カメラを使用するためカメラパーミッションの許可が必要です。
