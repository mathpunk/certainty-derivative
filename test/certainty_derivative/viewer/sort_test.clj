(ns certainty-derivative.viewer.sort-test
  (:require [certainty-derivative.record.example :refer [example-state]]
            [certainty-derivative.viewer.sort :refer :all]
            [clojure.test :refer [deftest is]]))

(deftest test-last-name-ascending-sort
  (let [expected '("Alberts" "Alexander" "Cunningham" "Henry" "Jenkins" "Wright")
        sorted (by-last-name example-state)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(deftest test-gender-sort
  (let [expected '("Alberts" "Wright" "Alexander" "Cunningham" "Henry" "Jenkins")
        sorted (by-gender-and-last-name example-state)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(deftest test-dob-sort
  (let [expected '("Alberts" "Jenkins" "Henry" "Wright" "Alexander" "Cunningham")
        sorted (by-date-of-birth example-state)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))


