(ns certainty-derivative.options
  (:require [clojure.java.io :as io]
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
