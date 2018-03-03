(ns certainty-derivative.options-test
  (:require  [certainty-derivative.options :refer :all]
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
  (is (not (s/valid? :certainty-derivative.options/sort "fname"))))

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
                   [:options :sort])))))
