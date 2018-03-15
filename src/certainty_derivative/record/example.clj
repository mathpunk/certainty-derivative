(ns certainty-derivative.record.example
  (:require [certainty-derivative.loader.transform :as xform]
            [certainty-derivative.viewer.format :as format]
            [clojure.string :as string]))

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

(def example-comma-rows ["Alexander, Mya, x, green, 1979-3-23"
                         "Henry, Giovanni, m, orange, 1941-7-16"])

(def example-space-rows ["Wright Camila f orange 1970-3-28"
                         "Cunningham Paul m green 1995-7-19"])

(def example-pipe-rows ["Alberts | Margaret | f | purple | 1902-9-2"
                        "Jenkins | James | m | red | 1928-2-13"])

(def example-input (concat example-comma-rows example-space-rows example-pipe-rows))

(def example-state (map xform/parse-row example-input))

(def example-json-output (map format/json-format example-state))

