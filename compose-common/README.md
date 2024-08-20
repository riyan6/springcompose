# common
## 加密解密
### 非对称加密
```java
public class Test {

    public static void main(String[] args) {
        RSA rsa = new RSA();

        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();

        System.out.println(publicKey);
        System.out.println(privateKey);

        String content = "WONIMAAIYOUWO";

        // 加密解密
        byte[] encrypt = rsa.encrypt(content.getBytes(), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

        System.out.println(new String(encrypt));
        System.out.println(new String(decrypt));
    }

}
```