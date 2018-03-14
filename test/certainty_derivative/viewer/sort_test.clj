(ns certainty-derivative.viewer.sort-test
  (:require [certainty-derivative.loader.transform :refer [string->date]]
            [certainty-derivative.record.example :refer [sample]]
            [certainty-derivative.viewer.sort :refer :all]
            [clojure.test :refer [deftest is testing]]))

(deftest test-last-name-ascending-sort
  (let [expected '("Alberts" "Alexander" "Cunningham" "Henry" "Wright")
        sorted (by-last-name sample)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(deftest test-gender-sort
  (let [expected '("Cunningham" "Wright" "Alberts" "Alexander" "Henry")
        sorted (by-gender-and-last-name sample)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(deftest test-dob-sort
  (let [expected '("Alberts" "Henry" "Wright" "Alexander" "Cunningham")
        sorted (by-date-of-birth sample)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

