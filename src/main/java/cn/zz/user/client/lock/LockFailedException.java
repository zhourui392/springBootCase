package cn.zz.user.client.lock;

/**
 * @author : zhourui
 * @version: 2019-09-20 11:47
 **/
public class LockFailedException extends Exception {
    public LockFailedException(String message) {
        super(message);
    }
}
