(def project '{{name}})
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources"}
          :source-paths #{"src"}
          :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                          [adzerk/boot-cljs-repl   "0.3.2" :scope "test"]
                          [adzerk/boot-reload "0.4.11" :scope "test"]
                          [com.cemerick/piggieback "0.2.1"  :scope "test"]
                          [weasel                  "0.7.0"  :scope "test"]

                          ;; clojure
                          [org.clojure/clojure "1.9.0-alpha10"]
                          [org.clojure/clojurescript "1.9.93"]
                          [org.clojure/core.async "0.2.385"]
                          [org.clojure/test.check "0.9.0" :scope "test"]
                          [org.clojure/tools.nrepl "0.2.12"]

                          ;; environment
                          [environ "1.0.3"]
                          [boot-environ "1.0.3"]

                          ;; ring-compojure server stuff
                          [ring/ring-core "1.4.0"]
                          [ring/ring-jetty-adapter "1.4.0"]
                          [ring/ring-defaults "0.1.5"]
                          [compojure "1.5.0"]

                          ;; server
                          [com.datomic/datomic-free   "0.9.5359"]

                          ;; styles
                          [garden "1.3.2"]
                          [org.martinklepsch/boot-garden "1.3.2-0"]

                          ;; client
                          [org.omcljs/om "1.0.0-alpha41"]])

#_(task-options!
   aot {:namespace   #{'{{namespace}}}}
   pom {:project     project
        :version     version
        :description "FIXME: write description"
        :url         "http://example/FIXME"
        :scm         {:url "https://github.com/yourname/{{name}}"}
        :license     {"Eclipse Public License"
                      "http://www.eclipse.org/legal/epl-v10.html"}}
   jar {:main        '{{namespace}}
        :file        (str "{{name}}-" version "-standalone.jar")})

(require
 '[clojure.pprint :refer [pprint]]
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[environ.boot :refer [environ]]
 '[org.martinklepsch.boot-garden :refer [garden]])

(def garden-prefixes
  [:transition
   :transition-duration
   :transition-property
   :transform
   :align-items
   :justify-content
   :flex-direction
   :flex
   :user-select])

(deftask dev
  "Run a restartable system in the Repl"
  []
  (comp
   (environ :env {:http-port "3000"
                  :db-uri "datomic:mem://{{name}}"})
   (watch :verbose true)
   (speak)
   (reload)
   (cljs :source-map true)
   (garden :styles-var '{{name}}.styles/base
           :output-to "css/styles.css"
           :vendors ["webkit" "moz"]
           :auto-prefix garden-prefixes)
   (repl :server true)))

(deftask dev-cljs-repl
  "Run a restartable system in the Repl"
  []
  (comp
   (environ :env {:http-port "3000"
                  :db-uri "datomic:mem://{{name}}"})
   (watch :verbose true)
   (speak)
   (reload)
   (cljs-repl)
   (cljs :source-map true)
   (garden :styles-var '{{name}}.styles/base
           :output-to "css/styles.css"
           :vendors ["webkit" "moz"]
           :auto-prefix garden-prefixes)))

(deftask dev-run
  "Run a dev system from the command line"
  []
  (comp
   (environ :env {:http-port "3000"})
   (cljs)
   (run :main-namespace "{{name}}.core" :arguments [#'dev-system])
   (wait)))

(deftask prod-run
  "Run a prod system from the command line"
  []
  (comp
   (environ :env {:http-port "8008"
                  :repl-port "8009"})
   (cljs :optimizations :advanced)
   (run :main-namespace "{{name}}.core" :arguments [#'prod-system])
   (wait)))

