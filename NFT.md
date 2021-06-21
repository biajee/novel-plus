Compile:
$ cd ~/novel/novel-plus/
$ mvn package

main webstie
$ cd ~/novel/novel-plus/novel-front/target
$ setsid java -jar novel-front-3.5.2.jar
Then, goto http://localhost:8080

crawler
$ cd ~/novel/novel-plus/novel-crawl/target
$ setsid java -jar novel-crawl-3.5.2.jar 	


admin
$ cd ~/novel/novel-plus/novel-admin/target
$ setsid java -jar novel-admin-3.5.2.jar