

docker rm -f web
docker run --name web  -p 80:80 -v /Users/albertlacambra/git/lacambra.tech/downloader/ui/src:/usr/share/nginx/html -d nginx