package github.A0CBEB339CB02898.randomusername;

/**
 * 用户名生成器异常类
 */
public class UsernameGeneratorException extends RuntimeException {
    public UsernameGeneratorException(String message) {
        super(message);
    }

    public UsernameGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
