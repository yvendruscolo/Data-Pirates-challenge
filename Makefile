.PHONY: build run

GENRES ?= action adventure animation biography comedy crime documentary drama family fantasy film_noir history horror music musical mystery news romance sci_fi sport thriller war western

arr.jar:
	docker run --rm -v `pwd`:/root/arr -w /root/arr clojure:boot-2.8.1-alpine boot build
	mv target/arr.jar arr.jar
	rm -r target

build: arr.jar
	@echo "arr.jar built!"

clean:
	rm -rf out

test: build clean
	docker run --rm -v `pwd`:/root/arr -w /root/arr clojure:boot-2.8.1-alpine boot test

run: build clean
	docker run --rm -v `pwd`/arr.jar:/opt/arr.jar -v `pwd`/out:/opt/out -w /opt clojure:boot-2.8.1-alpine java -jar arr.jar 500 $(GENRES)