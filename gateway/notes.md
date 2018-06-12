# stripPrefix

stripPrefix is confusing as there are 2 types and the docs dont make it clear as to whats happening
https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html


1. zuul.stripPrefix

zuul:
  prefix: /api
  strip-prefix: true (default)
  routes:
    ...
    
request: http://website:8080/api/v1/orders/new

by default, strip-prefix: true, so before matching any routes the /api prefix is removed. 
This means 'path' in each route definition will tried to be matched to /v1/orders/new. See next section for
how the route path matching logic works.


2. zuul.route.<route name>.stripPrefix

zuul:
  prefix: /api
  stripPrefix: true (default)
  routes:
    users:
      path: /myusers/**
      url: http://example.com/users_service
      stripPrefix: true (default)
      
The default for a route is stripPrefix=true. This means everything before the /** is stripped and forwarded on
downstream. 

The docs say: 
"Flag to determine whether the prefix for this route (the path, minus pattern patcher) should be stripped before forwarding."
so the path is /myusers and the pattern is /**. 
stripPrefix=true will give you everything matching the pattern /** with /myusers removed.
stripPrefix=false will give you /myusers/anything matching pattern.


For example consider.

http://api-gateway/api/myusers/top/users/today

1. /api prefix is removed by default by zuul.stripPrefix=true default
/myusers/top/users/today

2. /myusers/top/users/today matches the users route and stripPrefix=true by default so the request is forwarded
to the users_service with URI of /top/users/today
 
      
zuul:
  prefix: /api
  strip-prefix: true
  routes:
    multiplications:
      path: x/multiplications/**
      url: http://multiplication-service:8080
      stripPrefix: true


## So why would you strip prefixes?
since zuul is an api gateway, you can include the microservice name in the url but remove it so the downstream
microservice doesn't know its being forwarded requests from a gateway.

remember, zuul.stripPrefix and zuul.routes<name>.stripPrefix both default to true.

zuul:
  prefix: /api
  routes:
    orders:
      path: orders/**
      url: http://order-service

http://api-gateway/api/orders/current

### zuul then does this

1. strip /api off to get /orders/current
2. now match a path in routes which matchers the orders route.
so now this means 'orders' is stripped from the path and the values matching the patter /** are kept.

order-service is then forwarded on the URI request of /current which is what you would do if you were calling it as 
as single service without using microservices or a gateway.



### what about zuul.route<name>.stripPrefix=false

zuul:
  prefix: /api
  routes:
    orders:
      path: orders/**
      url: http://order-service
      stripPrefix: false
      
 1. strip /api off to get /orders/current (zuul.stripPrefix) applies to zuul.prefix only remember and the default is
 to strip it.
 
 2. the orders route path is matched, but this time everything before the pattern is kept, so 'orders' is kept 
 along with the pattern.
 
 so now orders-service gets forwarded on the request with URI /orders/current
 