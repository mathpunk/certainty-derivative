(ns certainty-derivative.transformer
  (:require certainty-derivative.record
            [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [java-time :as time]))

(defn detect-delimiter
  "Using the assumption that delimiter characters do not appear in the data
  values themselves."
  [input-row]
  (cond (string/includes? input-row "|") :pipe
        (string/includes? input-row ",") :comma
        :else :space))

(defmulti tokenize-row detect-delimiter)

(defmethod tokenize-row :space
  [input-row]
  (string/split input-row #" "))

(defmethod tokenize-row :comma
  [input-row]
  (string/split input-row #", "))

(defmethod tokenize-row :pipe
  [input-row]
  (string/split input-row #" \| "))

(defn string->date [s]
  (->> (string/split s #"-")
       (map #(Integer. %))
       (apply time/local-date)))

(defn parse-row [input-row]
  (let [fields (tokenize-row input-row)
        column-names [:certainty-derivative.record/last-name
                      :certainty-derivative.record/first-name
                      :certainty-derivative.record/gender
                      :certainty-derivative.record/favorite-color
                      :certainty-derivative.record/date-of-birth]
        string-record (zipmap column-names fields)]
    (update-in string-record
               [:certainty-derivative.record/date-of-birth] string->date)))

(s/fdef parse-row
        :ret :certainty-derivative.record/row)
