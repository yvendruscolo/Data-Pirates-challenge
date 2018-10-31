(ns arr.core
  "My solution to 'Data Pirates challenge', arr!"
  (:require [clojure.java.io :as io]
            [clojure.test :refer [is run-tests]]
            [cheshire.core :refer [generate-stream parsed-seq]]
            [hickory.core :refer [as-hiccup parse]])
  (:gen-class))

(defn url 
  "Recieves a film genre and a start number to return the url to get a block of 50 movies of descending order of rating, starting from the given number."
  [genre start]
  (str "https://www.imdb.com/search/title?"
       "title_type=feature&" ; set to ensure we are dealing with feature movies
       "num_votes=500,&" ; minimum number of votes, otherwise we get movies with handful random people rating them as 10
       "genres=" genre "&" ; our genre
       "start=" start "&" ; our "page"
       "sort=user_rating,desc&" ; to get in descending order of rating
       ))

(defn gets 
  "Just a faster, multi-arity version of get-in"
  [coll & idx] 
  (reduce get coll idx))

(def get-and-parse 
   "Recieves an url, and returns a semi-parsed block of 50 movies."
  (comp #(gets % 7 3 5 7 3 3 13 3) peek second as-hiccup parse slurp))

(defn title [data] (gets data 3 5 2))

(defn year [data] (-> (gets data 3 7 2) (clojure.string/replace #"[(|)]" "")))

(defn rating [data] (gets data 7 3 5 2))

(def movie-infos
  "Recieves a semi-parsed html element corresponding to a film, and returns a map of properties of that movie."
  (comp #(zipmap ["title" "year" "rating"] %)
        (juxt title year rating)))

(defn movies-by-genre 
  "The main pipeline, using functions above to generate our results."
  [genre file]
  (comp (map #(url genre %))
        (mapcat get-and-parse)
        (filter vector?)
        (map (comp #(do (generate-stream % file) (.write file "\n"))
                   movie-infos
                   #(get % 7)))))

(defn results 
  "Recieves a genre, and returns a function that keeps track of how many films we been through, and prints that when once we reached the end."
  [genre] 
  (fn ([n] (do (println genre "-- done --" n)) true) ([n _] (inc n))))

(defn movies 
  "The main wrapper around the pipeline, including the writting of output"
  [pages genre]
  (with-open [file (io/writer (str "out/" genre ".jsonl") :append true)]
   (transduce (movies-by-genre genre file)
              (results genre)
              0
              pages)))

(defn -main
  {:test (fn [& _]
           (let [genre (peek (shuffle ["action" "adventure" "animation" "biography" "comedy" "crime" "drama" "family" "fantasy" "film_noir" "history" "horror" "music" "musical" "mystery" "romance" "sci_fi" "sport" "thriller" "war" "western"]))
                 _ (-main "100" genre)]
             (with-open [file (io/reader (str "out/" genre ".jsonl"))]
               (mapv #(is (= (set (keys %)) #{"title" "year" "rating"}) 
                          (str "Oops, keys don't match!" %)) 
                     (parsed-seq file)))))}
  [n & genres]
  (if (= "test" (first genres)) (run-tests 'arr.core)
   (do (.mkdir (io/file "out"))
       (dorun (pmap #(movies (range 1 (inc (read-string n)) 50) %) genres))
       (shutdown-agents))))