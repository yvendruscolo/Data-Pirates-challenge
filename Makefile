.PHONY: build run

GENRES ?= action adventure animation biography comedy crime drama family fantasy film_noir history horror music musical mystery romance sci_fi sport thriller war western
IMAGE = clojure:boot-2.8.1-alpine

arr.jar:
	docker run -v `pwd`:/root/arr -w /root/arr $(IMAGE) boot build
	mv target/arr.jar arr.jar
	rm -r target

build: arr.jar
	@echo "arr.jar is built!"

clean:
	rm -r out

test: build
	docker run \
	-v `pwd`/arr.jar:/opt/arr.jar \
	-v `pwd`/out:/opt/out \
	-w /opt $(IMAGE) \
	java -jar arr.jar 100 test

run: build
	docker run \
	-v `pwd`/arr.jar:/opt/arr.jar \
	-v `pwd`/out:/opt/out \
	-w /opt $(IMAGE) \
	java -jar arr.jar 500 $(GENRES)