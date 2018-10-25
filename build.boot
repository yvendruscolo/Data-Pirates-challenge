(set-env! :source-paths #{"src"}
          :dependencies '[[hickory  "0.7.1"]
                          [cheshire  "5.8.1"]])


(task-options! aot {:namespace   #{'arr.core}}
               pom {:project     'arr
                    :version     "1.0.0"
                    :description "FIXME: write description"
                    :url         "http://example/FIXME"
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
