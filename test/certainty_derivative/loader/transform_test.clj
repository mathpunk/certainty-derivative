(ns certainty-derivative.loader.transform-test
  (:require [certainty-derivative.loader.transform :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [clojure.test :refer [deftest is]]))

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

(deftest test-format-classification
  (is (= :space (detect-delimiter example-space-row)))
  (is (= :pipe (detect-delimiter example-pipe-row)))
  (is (= :comma (detect-delimiter example-comma-row))))

(deftest test-row-parsing
  (is (s/valid? :certainty-derivative.record/row (parse-row example-space-row)))
  (is (s/valid? :certainty-derivative.record/row (parse-row example-comma-row)))
  (is (s/valid? :certainty-derivative.record/row (parse-row example-pipe-row))))
