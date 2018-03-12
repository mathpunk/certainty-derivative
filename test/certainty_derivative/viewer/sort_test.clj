(ns certainty-derivative.viewer.sort-test
  (:require [certainty-derivative.loader.transform :refer [string->date]]
            [certainty-derivative.record.example :refer [sample-input]]
            [certainty-derivative.viewer.sort :refer :all]
            [clojure.test :refer [deftest is testing]]))

(def sample (map (fn [record]
                   (update-in record
                              [:certainty-derivative.record/date-of-birth]
                              string->date)) sample-input))

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

(deftest test-sort-dispatch
  (testing "last name ascending"
    (let [expected '("Alberts" "Alexander" "Cunningham" "Henry" "Wright")
          sorted (dispatch-sort {:options {:sort "lname" :reverse false}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "last name descending"
    (let [expected (reverse '("Alberts" "Alexander" "Cunningham" "Henry" "Wright"))
          sorted (dispatch-sort {:options {:sort "lname" :reverse true}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "women first, otherwise by last name"
    (let [expected '("Cunningham" "Wright" "Alberts" "Alexander" "Henry")
          sorted (dispatch-sort {:options {:sort "women" :reverse false}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "date of birth ascending"
    (let [expected '("Alberts" "Henry" "Wright" "Alexander" "Cunningham")
          sorted (dispatch-sort {:options {:sort "dob" :reverse false}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted))))))
