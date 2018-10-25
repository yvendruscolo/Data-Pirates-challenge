.PHONY: build run

GENRES ?= action adventure animation biography comedy crime documentary drama family fantasy film_noir history horror music musical mystery news romance sci_fi sport thriller war western

arr.jar:
	docker run -v `pwd`:/root/arr -w /root/arr clojure:boot-2.8.1-alpine boot build
	mv target/arr.jar arr.jar
	rm -r target

build: arr.jar
	@echo "arr.jar built!" 

run: arr.jar
	java -jar target/arr.jar $(GENRES)