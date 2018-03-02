(ns certainty-derivative.core
  (:require [certainty-derivative.loader.read :refer [read-files]]
            [certainty-derivative.viewer.format :as format]))

(defn -main []
  (println "hello, -main"))

(defn render [records]
  (doseq [record records]
    (-> record
        format/friendly-format
        println)))

(defn view [& args]
  (let [records (apply read-files args)]
    (render records)))
