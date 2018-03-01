(ns certainty-derivative.core
  (:require [clojure.string :as string]))

(def example-first-name "Thomas")
(def example-last-name "Henderson")
(def example-favorite-color "purple")
(def example-gender "m")
(def example-date-of-birth "1976-03-23")

(def example-space-row (string/join " " [example-last-name
                                         example-first-name
                                         example-gender
                                         example-favorite-color
                                         example-date-of-birth]))

(def example-comma-row (string/join ", " [example-last-name
                                          example-first-name
                                          example-gender
                                          example-favorite-color
                                          example-date-of-birth]))

(def example-pipe-row (string/join " | " [example-last-name
                                          example-first-name
                                          example-gender
                                          example-favorite-color
                                          example-date-of-birth]))

(defn detect-delimiter
  "Using the assumption that delimiter characters do not appear in the data values themselves."
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

(tokenize-row example-comma-row)
(tokenize-row example-space-row)
(tokenize-row example-pipe-row)

