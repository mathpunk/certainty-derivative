(ns certainty-derivative.record.example
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


(def sample-input [{:certainty-derivative.record/last-name "Alexander"
                    :certainty-derivative.record/first-name "Mya"
                    :certainty-derivative.record/gender "x"
                    :certainty-derivative.record/favorite-color "green"
                    :certainty-derivative.record/date-of-birth "1979-3-23"},
                   {:certainty-derivative.record/last-name "Henry",
                    :certainty-derivative.record/first-name "Giovanni",
                    :certainty-derivative.record/gender "m",
                    :certainty-derivative.record/favorite-color "orange",
                    :certainty-derivative.record/date-of-birth "1941-7-16"},
                   {:certainty-derivative.record/last-name "Wright",
                    :certainty-derivative.record/first-name "Camila",
                    :certainty-derivative.record/gender "f",
                    :certainty-derivative.record/favorite-color "orange",
                    :certainty-derivative.record/date-of-birth "1970-3-28"},
                   {:certainty-derivative.record/last-name "Cunningham",
                    :certainty-derivative.record/first-name "Paul",
                    :certainty-derivative.record/gender "f",
                    :certainty-derivative.record/favorite-color "green",
                    :certainty-derivative.record/date-of-birth "1995-7-19"},
                   {:certainty-derivative.record/last-name "Alberts",
                    :certainty-derivative.record/first-name "Margaret",
                    :certainty-derivative.record/gender "m",
                    :certainty-derivative.record/favorite-color "purple",
                    :certainty-derivative.record/date-of-birth "1902-9-2"}])
