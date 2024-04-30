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
compose.yamlで最新のredisを起動して、保存したセッション情報をアプリケーション側の.dataにマウントして永続化してる

# テストとか
まだ書いてないので、今度書く
