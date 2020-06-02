# Spring Cloud Nginx Gateway

This project support config Nginx as a gateway automatically.
Can use the project for a replacement for Spring Cloud Gateway when you run it along with Nginx.
Currently, only implement Discovery Client for Eureka Discovery.

## Prerequisites

You have to set up a microservices project that has at least a Discovery Service.
The Discovery Service is using Eureka.

## Usage

Let say Nginx config locate at `/usr/local/etc/nginx/`.

1. Execute these commands:

    ```shell script
    $ cd /usr/local/etc/nginx/
    $ mkdir conf.d
    $ touch default.conf
    $ touch server.conf
    ```

2. Set `nginx.conf` like this:

    ```editorconfig
    events {
        worker_connections  1024;
    }
    
    http {
        include       mime.types;
    
        # Include default.conf here
    
        include       /usr/local/etc/nginx/conf.d/default.conf;
    
        default_type  application/octet-stream;
    
        sendfile        on;
    
        keepalive_timeout  65;
    
        #gzip  on;
    
        # Include server.conf here
        
        include       /usr/local/etc/nginx/conf.d/server.conf;
    
        server {
            listen       80;
            server_name  localhost;
    
            #charset koi8-r;
    
            #access_log  logs/host.access.log  main;
    
            location / {
                root   html;
                index  index.html index.htm;
            }
        }
    }
    ```

3. Add this configuration in `application.yml`
    
    ```yaml
    nginx:
      where: /usr/local/bin/nginx
      load-balancing-config-file: /usr/local/etc/nginx/conf.d/default.conf
      proxy-config-file: /usr/local/etc/nginx/conf.d/server.conf
      load-balancing-template: classpath:nginx/conf.d/default.conf
      server-template: classpath:nginx/conf.d/server.conf
      proxy-template: classpath:nginx/conf.d/location.conf
    ```



