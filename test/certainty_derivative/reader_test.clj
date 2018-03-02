(ns certainty-derivative.reader-test
  (:require  [certainty-derivative.reader :refer :all]
             [certainty-derivative.generator :refer [generate-test-data]]
             [clojure.spec.alpha :as s]
             [clojure.string :as string]
             [clojure.test :refer [deftest testing is]]
             [clojure.string :as string]))

(deftest test-file-reading
  (do
    (generate-test-data 10)
    (is (= 10 (count (read-file "./resources/001.txt"))))
    (is (= 30 (count (read-files "./resources/001.txt"
                                 "./resources/002.txt"
                                 "./resources/003.txt"))))))

(deftest test-file-parsing
  (do
    (generate-test-data 20)
    (let [data (read-files "./resources/001.txt"
                           "./resources/002.txt"
                           "./resources/003.txt")]
      (is (every? #(s/valid? :certainty-derivative.input/row %) data)))))
