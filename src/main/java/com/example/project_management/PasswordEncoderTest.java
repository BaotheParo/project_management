import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123"; // Thay bằng mật khẩu thực
        String hashedPassword = encoder.encode(password);
        System.out.println(hashedPassword);
    }
}