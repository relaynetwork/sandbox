# auth-server-example

Reference implementation for session based authentication server

## Usage

lein run

There is no restriction on /public/stuff:

> curl -XGET -H 'Content-type: application/json' -i http://localhost:8282/public/stuff

HTTP/1.1 200 OK
Date: Thu, 30 Mar 2017 18:22:57 GMT
Content-Type: application/json;charset=utf-8
Content-Length: 36
Server: Jetty(9.4.3.v20170317)

{"message":"everyone can see this!"}


There is one route (/restricted/thing) that requires you to authenticate to access it:

> curl -XGET -H 'Content-type: application/json' -i http://localhost:8282/restricted/thing

HTTP/1.1 401 Unauthorized
Date: Thu, 30 Mar 2017 18:24:30 GMT
Content-Length: 0
Server: Jetty(9.4.3.v20170317)

We must first login. There is only one valid user/pass combo  (jc/kool).

> curl -XPOST -H 'Content-type: application/json' -i -d '{"username":"jc","password":"kool"}' http://localhost:8282/session

HTTP/1.1 200 OK
Date: Thu, 30 Mar 2017 18:25:54 GMT
Set-Cookie: id=%3A4f0330b5-6311-47bc-be97-8f50b3d94aed;Path=/;HttpOnly;Secure
Content-Type: application/json;charset=utf-8
Content-Length: 22
Server: Jetty(9.4.3.v20170317)

{"authenticated":true}


Now we can access /restricted/thing using our new session:

> curl -XGET -H 'Content-type: application/json' -H 'Cookie: id=%3A4f0330b5-6311-47bc-be97-8f50b3d94aed' -i http://localhost:8282/restricted/thing

HTTP/1.1 200 OK
Date: Thu, 30 Mar 2017 18:26:43 GMT
Content-Type: application/json;charset=utf-8
Content-Length: 40
Server: Jetty(9.4.3.v20170317)

{"secret-message":"drink your ovaltine"}


## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
