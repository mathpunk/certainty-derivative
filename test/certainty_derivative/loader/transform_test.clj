(ns certainty-derivative.loader.transform-test
  (:require [certainty-derivative.loader.transform :refer :all]
            [certainty-derivative.record.example
             :refer
             [example-comma-row example-pipe-row example-space-row]]
            [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]))

(deftest test-format-classification
  (is (= :space (detect-delimiter example-space-row)))
  (is (= :pipe (detect-delimiter example-pipe-row)))
  (is (= :comma (detect-delimiter example-comma-row))))

(deftest test-row-parsing
  (is (s/valid? :certainty-derivative.record/row (parse-row example-space-row)))
  (is (s/valid? :certainty-derivative.record/row (parse-row example-comma-row)))
  (is (s/valid? :certainty-derivative.record/row (parse-row example-pipe-row))))
