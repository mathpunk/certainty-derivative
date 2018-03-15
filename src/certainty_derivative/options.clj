(ns certainty-derivative.options
  (:require [certainty-derivative.viewer.sort :as sort]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.tools.cli :refer [parse-opts]]))

(s/def ::sort #{"women" "lname" "dob"})

(s/def ::file (fn [filename] (.exists (io/file filename))))

(s/def ::files (s/coll-of ::file))

(s/def ::parsed (s/keys :req-un [::files ::ordering]))

(def flag-options
  [["-s" "--sort ORDERING" "Sort by 'women', 'dob', or 'lname' (default)"
    :default "lname"
    :validate [(fn [sort] (#{"women" "lname" "dob"} sort))
               "Supported orderings: 'women', 'dob', or 'lname'"]]
   ["-r" "--reverse" "Reverse the sort order (default false)"
    :default false]
   ["-h" "--help"]])

(s/fdef flag-options
        :ret ::parsed)

(defn parse [args]
  (parse-opts args flag-options))

(defn dispatch-sort [options records]
  (let [strategy (get-in options [:options :sort])
        direction (if (get-in options [:options :reverse])
                    reverse
                    identity)]
    (case strategy
      "women" (direction (sort/by-gender-and-last-name records))
      "lname" (direction (sort/by-last-name records))
      "dob" (direction (sort/by-date-of-birth records)))))


#_(parse ["-r" "true" "./resources/001.txt" "./resources/002.txt"])

#_(parse ["-s" "women" "./resources/001.txt"])

#_(parse ["-s" "rhubarb" "./resources/001.txt"])

#_(parse ["-s" "dob" "./resources/001.txt" "./resources/002.txt"])

#_(parse ["-s" "dob" "./resources/001.txt" "./resources/002.txt"])
