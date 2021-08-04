ssh ubuntu@44.233.171.170

Compile:
$ cd ~/novel/novel-plus/
$ mvn package

main webstie
$ cd ~/novel/novel-plus/novel-front/target
$ setsid java -jar novel-front-3.5.4.jar
Then, goto http://localhost:8080

crawler
$ cd ~/novel/novel-plus/novel-crawl/target
$ setsid java -jar novel-crawl-3.5.4.jar 	


admin
$ cd ~/novel/novel-plus/novel-admin/target
$ setsid java -jar novel-admin-3.5.4.jar


GRANT ALL PRIVILEGES on . to root@'ec2-44-233-171-170.us-west-2.compute.amazonaws.com' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES on . to root@ IDENTIFIED BY  WITH GRANT OPTION;
GRANT ALL PRIVILEGES on *.* to 'root'@'%' WITH GRANT OPTION;

public key 33d89eaca73844b979a1fb59b3729cbc8b9e89a5d6c1d745669ad90b05c0439cbb62819098ada6200fce504fe37c95c2f6cc6226aed1c7a31a69102e056a65f3
private key 8241f5ca557d34336c50710fcbec04d5cd6612789fa19e23b42a090dea64f9c1
address 0x1cc5407e1b809bceee0d78cd17edc9e35b52c837


/usr/local/opt/util-linux/bin/setsid for mac

http://44.233.171.170:8080

13035326187
test123
 No newline at end of file


mysqladmin -u root -p create novel_blockchain
mysqldump -u root -v books -p | mysql -u root -p -D novel_blockchain

mysqladmin -u root -p drop novel_blockchain
mysqladmin -u root -p create novel_blockchain
mysql -u root -p novel_blockchain < /home/ubuntu/novel/novel-plus/doc/sql/novel_blockchain.sql

sudo /etc/init.d/mysql start