(ns arr.core
  "My solution to 'Data Pirates challenge', arr!"
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.test :refer [is]]
            [cheshire.core :refer [generate-stream parsed-seq]]
            [hickory.core :refer [as-hiccup parse]])
  (:gen-class))

(defn url 
  "Recieves a film genre and page number to return the url to get the nth page of descending order of rating, containing 50 titles at a time as of this writing."
  [genre page]
  (str "https://www.imdb.com/search/title?"
       "genres=" genre
       "&explore=title_type,genres&pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=75c37eae-37a7-4027-a7ca-3fd76067dd90&pf_rd_r=87CERCHXAK2JPTZJA1A2&pf_rd_s=center-1&pf_rd_t=15051&pf_rd_i=genre&title_type=movie&sort=user_rating,desc&"
       "page=" page
       "&ref_=adv_nxt"))

(defn gets 
  "Just a faster, multi-arity version of get-in"
  [coll & idx] 
  (reduce get coll idx))

(def get-and-parse 
   "Recieves an url, and returns a semi-parsed block of 50 movies."
  (comp #(gets % 7 3 5 5 3 3 7 7) peek second as-hiccup parse slurp))

(def movie-infos
  "Recieves a semi-parsed html element corresponding to a film, and returns a map of properties of that movie."
  (comp #(zipmap ["title" "year" "duration" "rating"] %)
        (juxt #(gets % 3 5 2)
              (comp #(if (vector? %) (peek %) %) #(s/split % #"[(|)]") #(gets %  3 7 2))
              (comp #(if (s/includes? % "min") % "No duration info") #(gets % 5 3 2))
              #(gets % 7 3 5 2))))

(defn movies-by-genre 
  "The main pipeline, using functions above to generate our results."
  [genre file]
  (comp (map #(url (s/capitalize genre) %))
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
           (let [genre (peek (shuffle ["action" "adventure" "animation" "biography" "comedy" "crime" "documentary" "drama" "family" "fantasy" "film_noir" "history" "horror" "music" "musical" "mystery" "news" "romance" "sci_fi" "sport" "thriller" "war" "western"]))
                 _ (-main "100" genre)]
             (with-open [file (io/reader (str "out/" genre ".jsonl"))]
               (mapv #(is (= (set (keys %)) #{"title" "year" "duration" "rating"}) 
                          (str "Oops, keys don't match!" %)) 
                     (parsed-seq file)))))}
  [n & genres] 
  (do (.mkdir (io/file "out")) 
	  (dorun (pmap #(movies (range 1 (inc (/ (read-string n) 50))) %) genres))
	  (shutdown-agents)))