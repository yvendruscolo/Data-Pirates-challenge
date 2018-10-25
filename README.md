# My solution to the [Data Pirates challenge](https://github.com/NeowayLabs/jobs/blob/master/datapirates/challenge.md)

## Language: Clojure

#### *What?*

A Command Line Interface to lookup the 500 (actually 10 first pages from the) Top Ranked Movies in [IMDb](https://www.imdb.com/feature/genre/) for any (reasonable) amout of given genres (case sensitive), and spit them into a jsonl file for each genre

#### *But why?*

* Because I think it's awesome and more people should be aware of it.
  
* Quite easy for concurrency/paralelism, like Go.

* Lisp has great expressive power.

#### *And how?*

* It can be run with `make run` (needs [Boot](https://github.com/boot-clj)), which will trigger `make build` to generate the jar file to be run by java