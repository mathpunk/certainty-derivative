(ns certainty-derivative.core
  (:require [certainty-derivative.loader.read :refer [read-files]]
            [certainty-derivative.options :as options]
            [certainty-derivative.viewer.sort :as sort]
            [clojure.tools.cli :refer [parse-opts]]
            [certainty-derivative.viewer.format :as format]))

(defn -main []
  (println "hello, -main"))

(defn render [records]
  (doseq [record records]
    (-> record
        format/friendly-format
        println)))

(defn view [& args]
  (let [command (options/parse args)
        records (apply read-files (command :arguments))
        sorted (sort/dispatch-sort command records)]
    (render sorted)))
