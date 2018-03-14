(ns certainty-derivative.options-test
  (:require  [certainty-derivative.options :refer :all]
             [certainty-derivative.record.example :refer [example-state]]
             [clojure.test :refer [deftest testing is]]
             [clojure.spec.alpha :as s]
             [clojure.string :as string]))

(deftest test-argument-elements
  (is (s/valid? :certainty-derivative.options/file
                "./resources/001.txt"))
  (is (s/valid? :certainty-derivative.options/files
                '("./resources/002.txt" "./resources/001.txt" )))
  (is (not (s/valid? :certainty-derivative.options/files
                     '("./resources/not-found.txt" "./resources/001.txt")))))

(deftest test-option-elements
  (is (s/valid? :certainty-derivative.options/sort "lname"))
  (is (not (s/valid? :certainty-derivative.options/sort "fname")))) ;; Not implemented

(deftest test-parsed-arguments
  (is (some #{"./resources/001.txt"}
            (get (parse ["-s" "lname" "./resources/001.txt" "./resources/002.txt"])
                 :arguments))))

(deftest test-options
  (testing "with no sort option given"
    (is (= "lname"
           (get-in (parse ["./resources/001.txt" "./resources/002.txt"])
                   [:options :sort]))))
  (testing "with sort option (-s flag)"
    (is (= "women"
           (get-in (parse ["-s" "women" "./resources/001.txt" "./resources/002.txt"])
                   [:options :sort]))))
  (testing "with reversal"
    (is (get-in (parse ["-r" "./resources/001.txt"])
                [:options :reverse]))
    (is (not (get-in (parse ["./resources/001.txt"])
                     [:options :reverse])))))

(deftest test-sort-dispatch
  (testing "last name ascending"
    (let [expected '("Alberts" "Alexander" "Cunningham" "Henry" "Jenkins" "Wright")
          sorted (dispatch-sort {:options {:sort "lname" :reverse false}} example-state)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "last name descending"
    (let [expected (reverse '("Alberts" "Alexander" "Cunningham" "Henry" "Jenkins" "Wright"))
          sorted (dispatch-sort {:options {:sort "lname" :reverse true}} example-state)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "women first, otherwise by last name"
    (let [expected '("Alberts" "Wright" "Alexander" "Cunningham" "Henry" "Jenkins")
          sorted (dispatch-sort {:options {:sort "women" :reverse false}} example-state)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "date of birth ascending"
    (let [expected '("Alberts" "Jenkins" "Henry" "Wright" "Alexander" "Cunningham")
          sorted (dispatch-sort {:options {:sort "dob" :reverse false}} example-state)]
      (is (= expected (map :certainty-derivative.record/last-name sorted))))))
