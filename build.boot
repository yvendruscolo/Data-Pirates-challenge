(set-env! :source-paths #{"src"}
          :dependencies '[[org.clojure/clojure "1.9.0"]
                          [adzerk/boot-test "1.2.0" :scope "test"]
                          [hickory  "0.7.1"]
                          [cheshire  "5.8.1"]])

(require '[adzerk.boot-test :refer :all])

(task-options! aot {:namespace   #{'arr.core}}
               pom {:project     'arr
                    :version     "1.0.0"
                    :description "Solution to the Data Pirates challenge"
                    :url         "https://github.com/yvendruscolo/Data-Pirates-challenge"
                    :scm         {:url "https://github.com/yvendruscolo/Data-Pirates-challenge"}
                    :license     {"Eclipse Public License"
                                  "http://www.eclipse.org/legal/epl-v10.html"}}
               jar {:main        'arr.core
                    :file        "arr.jar"})


(deftask build []
  (comp (aot)
        (pom)
        (uber)
        (jar)
        (target)))
