**Инструкция по работе с базой данных PostgreSQL**
1. Подключение к базе данных в Java
```
private static final String URL = "jdbc:postgresql://localhost:5432/" + System.getenv("POSTGRES_DB");
private static final String USER = System.getenv("POSTGRES_USER");
private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");
```
2. Работа с данными
Сохранение пользователя (примерный код):
```
public void saveUser(String nickname, String interests) {
    String sql = "INSERT INTO users (nickname, interests) VALUES (?, ?)";
    try (Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, nickname);
        pstmt.setString(2, interests);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
    }
}
```
Получение данных пользователя (примерный код):
```
public User getUser(String nickname) {
    String sql = "SELECT nickname, interests FROM users WHERE nickname = ?";
    try (Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, nickname);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new User(rs.getString("nickname"), rs.getString("interests"));
        }
    } catch (SQLException e) {
        System.out.println("Ошибка при получении пользователя: " + e.getMessage());
    }
    return null;
}
```
3. Основные команды:
Запуск контейнера:
```
docker-compose up -d
```
Проверка состояния контейнера:
```
docker-compose ps
```
Остановка контейнеров:
```
docker-compose down
```
