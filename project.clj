(defproject certainty-derivative "0.1.0-SNAPSHOT"
  :description "A sample app for parsing and serving text data."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.json "0.2.6"]
                 [clojure.java-time "0.3.1"]
                 [org.clojure/tools.cli "0.3.5"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-mock "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [cheshire "5.8.0"]
                 [compojure "1.6.0"]
                 [hiccup "1.0.5"]
                 ]
  :plugins [[lein-ring "0.12.3"]]
  :ring {:init certainty-derivative.generator/init
         :handler certainty-derivative.server/app}
  :aliases {"view" ["run" "-m" "certainty-derivative.core/view"]
            "data" ["run" "-m" "certainty-derivative.generator/init"]
            "serve" ["ring" "server"]}
  )
