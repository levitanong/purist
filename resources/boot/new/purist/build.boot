(def project '{{name}})
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{
                            {{#server}}
                            "src/clj"
                            {{/server}}
                            "resources"}
          :source-paths #{"src/cljs" "src/garden"}
          :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                          [adzerk/boot-cljs-repl   "0.3.3" :scope "test"]
                          [adzerk/boot-reload "0.4.11" :scope "test"]
                          [com.cemerick/piggieback "0.2.1"  :scope "test"]
                          [weasel                  "0.7.0"  :scope "test"]
                          {{#client-only}}
                          [pandeiro/boot-http "0.7.3" :scope "test"]
                          {{/client-only}}

                          ;; clojure
                          [org.clojure/clojure "1.9.0-alpha11"]
                          [org.clojure/clojurescript "1.9.225"]
                          [org.clojure/core.async "0.2.385"]
                          [org.clojure/test.check "0.9.0" :scope "test"]
                          [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                          {{#server}}
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

                          {{/server}}
                          ;; styles
                          [garden "1.3.0"]
                          [org.martinklepsch/boot-garden "1.3.0-0"]

                          ;; client
                          [org.omcljs/om "1.0.0-alpha41"]
                          [sablono "0.7.3"]
                          [binaryage/devtools "0.8.1" :scope "test"]])

{{#server}}
(task-options!
   aot {:namespace   #{'{{name}}}}
   pom {:project     project
        :version     version
        :description "FIXME: write description"
        :url         "http://example/FIXME"
        :scm         {:url "https://github.com/yourname/{{name}}"}
        :license     {"Eclipse Public License"
                      "http://www.eclipse.org/legal/epl-v10.html"}}
   jar {:main        '{{name}}
        :file        (str "{{name}}-" version "-standalone.jar")})
{{/server}}

(require
 '[clojure.pprint :refer [pprint]]
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 {{#server}}
 '[environ.boot :refer [environ]]
 {{/server}}
 '[org.martinklepsch.boot-garden :refer [garden]]
 {{#client-only}}
 '[pandeiro.boot-http    :refer [serve]]
 {{/client-only}}
 '[danielsz.autoprefixer :refer [autoprefixer]]
 )

{{#client-only}}
(deftask dev
  "Run app"
  []
  (comp
   (serve)
   (watch)
   (speak)
   (reload :on-jsload '{{name}}.core/main)
   (cljs-repl)
   (cljs :source-map true :optimizations :none)
   (garden :styles-var '{{name}}.styles/base
     :output-to "css/styles.css")
   (autoprefixer :files ["styles.css"])))
{{/client-only}}

{{#server}}
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
           :output-to "css/styles.css")
   (autoprefixer :files ["styles.css"])
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
     :output-to "css/styles.css")
   (autoprefixer :files ["styles.css"])))

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
{{/server}}
