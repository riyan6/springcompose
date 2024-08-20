package org.spring.compose.common.crypto;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * 对称加密工具类
 * <p>AES对比DES</p>
 * <ul>
 *     <li>AES 比 DES 更安全，AES支持更长的密钥长度</li>
 *     <li>AES 的加密速度和效率要快于 DES</li>
 *     <li>AES 逐渐取代 DES</li>
 * </ul>
 *
 * @author riyan6
 * @see <a href="https://plus.hutool.cn/pages/782820/#%E5%AF%B9%E7%A7%B0%E5%8A%A0%E5%AF%86">Doc</a>
 * @since 2024/4/22
 */
public class SymmetricEncryptionUtil {

    /**
     * DES 对称加密工具类
     */
    public static class DES {
        /**
         * des 加密
         *
         * @param secret
         * @param str
         * @return
         */
        public static String encode(String secret, String str) {
            SymmetricCrypto des = SecureUtil.des(secret.getBytes(StandardCharsets.UTF_8));
            return des.encryptHex(str);
        }

        /**
         * des 解密
         *
         * @param secret
         * @param str
         * @return
         */
        public static String decode(String secret, String str) {
            SymmetricCrypto des = SecureUtil.des(secret.getBytes(StandardCharsets.UTF_8));
            return des.decryptStr(str, CharsetUtil.CHARSET_UTF_8);
        }
    }

    /**
     * AES 对称加密工具类
     */
    public static class AES {
        /**
         * AES 加密
         * <p><b>AES加密算法规定的密钥长度只能是16字节（对应128位）、24字节（对应192位）或32字节（对应256位）</b></p>
         *
         * @param secret 密钥
         * @param str    需要加密的内容
         * @return 加密完毕的字符串
         */
        public static String encode(String secret, String str) {
            // 使用SHA-256生成固定长度的密钥
            byte[] keyBytes = SecureUtil.sha256().digest(secret.getBytes(StandardCharsets.UTF_8));
            SymmetricCrypto aes = SecureUtil.aes(keyBytes);
            return aes.encryptHex(str);
        }

        /**
         * DES 解密
         * <p><b>AES加密算法规定的密钥长度只能是16字节（对应128位）、24字节（对应192位）或32字节（对应256位）</b></p>
         *
         * @param secret 密钥
         * @param str    加密字符串
         * @return 解密完毕的字符串
         */
        public static String decode(String secret, String str) {
            byte[] keyBytes = SecureUtil.sha256().digest(secret.getBytes(StandardCharsets.UTF_8));
            SymmetricCrypto aes = SecureUtil.aes(keyBytes);
            return aes.decryptStr(str, CharsetUtil.CHARSET_UTF_8);
        }
    }
}
