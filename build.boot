(set-env! :source-paths #{"src"}
          :dependencies '[[org.clojure/clojure "1.9.0"]
                          [hickory  "0.7.1" :exclusions [org.clojure/clojurescript viebel/codox-klipse-theme]]
                          ; hickory works on both clojure and clojurescript, so it has the clojurescript compiler and the google closure optimizer as default dependencies, now explicitly taking it off
                          [cheshire  "5.8.1"]])


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
