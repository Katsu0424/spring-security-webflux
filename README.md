# エンドポイント
## /login
curlから叩くので、クライアント側のセッションID置き場としてcookie.txtを指定してる　<br>
-c cookie.txtを指定せずにヘッダー情報のセッションIDを直接使ってもOK
```
curl -XPOST -i -c cookie.txt -H 'Content-Type: application/json' \
-d '{"mail_address": "test@test.com", "password": "password"}' \
localhost:8080/login
```

## /hello
-b cookie.txtではなく-b 'SESSION=[セッションID]'でもOK
```
curl -i -b cookie.txt localhost:8080/hello
```

# Redis(セッション管理)
compose.yamlで最新のredisを起動して、保存したセッション情報をアプリケーション側の.dataにマウントして永続化してる　<br>
今は同一リポジトリで管理してるが、外部に立ち上げて繋ぐようにしても良いかも　<br>
(lettuceConnectionFactoryで設定してapplication.yamlに接続情報書くとかでいけそう？)

# テストとか
まだ書いてないので、今度書く


**参考**)[見ないようにしていたSpring Securityの認証実装と向き合った話(WebFlux)](https://zenn.dev/jy8752/articles/3b5beaced81efd)
