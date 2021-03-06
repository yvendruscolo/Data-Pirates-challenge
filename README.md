# My solution to the [Data Pirates challenge](https://github.com/NeowayLabs/jobs/blob/master/datapirates/challenge.md)

## Language: Clojure

#### *What?*

A Command Line Interface to lookup the 500 (actually 10 first pages from the) Top Ranked Movies in [IMDb](https://www.imdb.com/feature/genre/) for any (reasonable) amout of given genres (case sensitive), and spit them into a jsonl file for each genre

#### *But why?*

* Because I think it's awesome and more people should be aware of it.
  
* Quite easy for concurrency/paralelism, like Go.

* Lisp has great expressive power.

#### *And how?*

All you need is:

1. Make
2. Docker
3. Internet connection

And then use `make run`. Output will be in the **out** folder.
If you need to change the genres, either set an environment variable `GENRES`, change the makefile or use `make run GENRES="action comedy"`.

You can also use `make test` which will check if IMDb's api is still compatible, by running a slimmed version of the application for a random valid genre and checking the results