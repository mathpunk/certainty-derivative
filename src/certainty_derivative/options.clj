(ns certainty-derivative.options
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.spec.alpha :as s]
            [clojure.java.io :as io]))

(s/def ::sort #{"women" "lname" "dob"})

(s/def ::file (fn [filename] (.exists (io/file filename))))

(s/def ::files (s/coll-of ::file))

(s/def ::parsed (s/keys :req-un [::files ::ordering]))

(def flag-options
  [["-s" "--sort ORDERING" "Sort by 'women', 'dob', or 'lname' (default)"
    :default "lname"
    #_:validate #_[#(s/valid? :certainty-derivative.options/ordering %)
                   "Must be 'women', 'dob', or 'lname'"]]
   ["-h" "--help"]])

(s/fdef flag-options
        :ret ::parsed)

(defn parse [args]
  (parse-opts args flag-options))

#_(parse ["./resources/001.txt" "./resources/002.txt"])

#_(parse ["-s" "women" "./resources/001.txt"])

#_(parse ["-s" "dob" "./resources/001.txt" "./resources/002.txt"])

